# java中并发工具类

CountDownLatch、CycleBarrier、Semaphore工具类提供一种并发流程控制的手段，Exchanger工具类提供了在线程间交换数据的一种手段
## 等待多线程完成的CountDownLatch

CountDownLatch允许一个或多个线程等待其他线程完成操作。  
构造函数接受一个int类型的参数作为计数器，等待N个点完成，就传入N；  
调用countDown方法时，N就会减1，CounDownLatch的await方法会阻塞当前线程，直到N为0；这里的N个点可以是N个线程，也可以是1个线程里的N个步骤。  
不能让主线程一直等待，也提供带指定时间的await()——await(long time, TimeUnit unit);

```java
/**
 * Created by husheng02.
 * join用于让当前线程等待join线程执行结束，其原理就是不停检查join线程是否存活
 * @author <a href="mailto:husheng3921@163.com">胡圣</a>
 * @date 2020/08/07 16:16
 */
public class JoinCountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
            }
        });
        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("2");
            }
        });
        th1.start();
        th2.start();
        th1.join();
        th2.join();
        System.out.println("all thread finish");
    }
}

//一个线程里N个步骤
public class CountDownLatchTest {
    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                c.countDown();
                System.out.println(2);
                c.countDown();
            }
        }).start();
        c.await();
        System.out.println("3");
    }
}
//一个线程等待N个线程
public class CountDownLatchTest1 {
    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                c.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(2);
                c.countDown();
            }
        }).start();

        c.await();
        System.out.println("3");
    }

}
//多个线程等待一个线程
public class CountDownLatchTest2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("所有运动员有5秒的准备时间");
        CountDownLatch c = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i< 5; i++){
            final int no = i +1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println( no +"号远动员准备完毕，等待");
                    try{
                        //调用await()方法开始等待
                        c.await();
                        System.out.println(no+"号远动员开始跑");
                    }catch (InterruptedException e){

                    }
                }
            };
            service.execute(runnable);
        }
        Thread.sleep(5000);
        System.out.println("5秒准备时间已经过去，发令枪响");
        //调用countDown方法，之前await被唤醒
        c.countDown();
    }
}
```

## 同步屏障CycleBarrier

* 可循环使用的屏障，让一组线程到达一个屏障时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被拦截的线程才会继续运行。  
* CycleBarrier可以用于多线程计算数据，最后合并计算结果的场景。

```java
public class BankWaterService implements Runnable {
    //创建4个屏障，this指的是执行动作barrierAction,传入的Runnable对象
    private CyclicBarrier c = new CyclicBarrier(4, this);
    // 只有4个sheet，启动4个线程
    private ExecutorService threadPool = Executors.newFixedThreadPool(4);
    //保存每个sheet计算出的结果
    private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    private void count(){
        for (int i = 0; i<4; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    map.put(Thread.currentThread().getName(), 1);
                    //计算完成，插入一个屏障
                    try{
                        c.await();
                    }catch (InterruptedException | BrokenBarrierException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    @Override
    public void run() {
        int result = 0;
        //汇总每个Sheet计算结果
        for(Map.Entry<String, Integer> sheet : map.entrySet()){
            result += sheet.getValue();
        }
        map.put("result", result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
```
CountdownLatch与CycleBarrier异同：
* 相同：都能阻塞一个或一组线程，直到某个预设条件达成发生，再统一出发
* 不同：1、作用对象不同；CycleBarrier要等固定的数量线程都到达栅栏位置才继续执行，而CountdownLatch只需要等待数字倒数到0；CycleBarrier作用于线程，CountdownLatch作用于事件；CountdownLatch在调用Countdown方法后把数字减一；CycleBarrier在某线程开始后，把计数减一
* 不同：2、可重用性不同，CountdownLatch倒数为0出发门打开后，不能再次使用；CycleBarrier可以重复使用，采用reset()。

## 控制并发线程数的Semaphore
* Semaphore信号量用来控制同时访问特定资源的线程数量，它通过协调各个线程，保证合理使用公共资源acquire(),release();
* 用于流量控制，特别是公共资源例如数据库连接

```java
public class SemaphoreTest {
    private static final int THREAD_COUNT = 30;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for(int i = 0; i < THREAD_COUNT; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        s.acquire();
                        System.out.println("save data");
                        s.release();
                    }catch (InterruptedException e){

                    }
                }
            });
        }
        threadPool.shutdown();
    }
}

```
* tryAcquire()尝试去获取许可证，没有空闲的不必陷入阻塞，可以去做别的事情
* tryAcquire(long timeout, TimeUnit unit);传入超时时间，最多等待timeout时间获取许可证，超时则获取失败返回false；
* availablePermits()返货可用许可证数量

信号量注意点：
* 获取和释放的许可证数量尽量保持一致，
* 初始化的时候可以设置公平性，如果设置true，则会让它公平，但是如果设置false，则会让吞吐量更高
* 信号量支持跨线程、线程池的，并不是哪个线程获取许可证就必须由这个线程去释放。
## 线程间交换数据的Exchanger
* Exchange用于进程间的数据交换，它提供一个同步点，两个线程可以交换彼此的数据，通过exchange方法交换数据
 * 如果第一个线程先执行exchange(),它会一直等待第二个线程也执行exchange方法，当两个线程都同时都到达同步点，就可以交换数据，将本线程产生出来的数据传递给对方。

```java
public class ExchangerTest {
    private static final Exchanger<String> exgre = new Exchanger<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String A = "银行流水A";
                    exgre.exchange(A);
                }catch (InterruptedException e){

                }
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                String B = "银行流水B";
                try {
                    Thread.sleep(5000);
                    String A = exgre.exchange(B);
                    System.out.printf("A和B数据是否一致" + A.equals(B)+",A录入的是"+ A+",B录入的是"+B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPool.shutdown();
    }
}
```