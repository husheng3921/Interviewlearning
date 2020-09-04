# CAS

CAS : Compare-And-Swap 比较并交换，乐观锁的实现；CAS的特点是避免使用互斥锁，当多个线程同时使用CAS更新同一个变量时，只有一个线程能操作成功，其他线程都会更新失败，但是更新失败的线程不会被阻塞，而是被告知竞争失败，可以再次尝试。  

## CAS思路
CAS三个操作数:内存值V、预期值A、要修改的值B。最核心的思想：<strong>仅当预期值A和当前的内存值V相同时，才将内存值修改为B</strong>  

我们对此展开描述一下：CAS 会提前假定当前内存值 V 应该等于值 A，而值 A 往往是之前读取到当时的内存值 V。在执行 CAS 时，如果发现当前的内存值 V 恰好是值 A 的话，那 CAS 就会把内存值 V 改成值 B，而值 B 往往是在拿到值 A 后，在值 A 的基础上经过计算而得到的。如果执行 CAS 时发现此时内存值 V 不等于值 A，则说明在刚才计算 B 的期间内，内存值已经被其他线程修改过了，那么本次 CAS 就不应该再修改了，可以避免多人同时修改导致出错。这就是 CAS 的主要思路和流程。

JDK 正是利用了这些 CAS 指令，可以实现并发的数据结构，比如 AtomicInteger 等原子类。

利用 CAS 实现的无锁算法，就像我们谈判的时候，用一种非常乐观的方式去协商，彼此之间很友好，这次没谈成，还可以重试。CAS 的思路和之前的互斥锁是两种完全不同的思路，如果是互斥锁，不存在协商机制，大家都会尝试抢占资源，如果抢到了，在操作完成前，会把这个资源牢牢的攥在自己的手里。当然，利用 CAS 和利用互斥锁，都可以保证并发安全，它们是实现同一目标的不同手段.

## CAS语义

 compareAndSwap 方法是被 synchronized 修饰的，我们用同步方法为 CAS 的等价代码保证了原子性。

 ```java
 public class DebugCAS implements Runnable {
    private volatile int value;
    public synchronized int compareAndSwap(int expectValue, int newValue){
        int oldValue = value;
        if (oldValue == expectValue){
            value = newValue;
            System.out.println("线程"+Thread.currentThread().getName()+"执行");
        }
        return oldValue;
    }
    @Override
    public void run() {
        compareAndSwap(100, 150);
    }

    public static void main(String[] args) throws InterruptedException{
        DebugCAS r = new DebugCAS();
        r.value = 100;
        Thread t1 = new Thread(r, "Thread 1");
        Thread t2 = new Thread(r, "Thread 2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(r.value);
    }
}

```

## CAS和乐观锁，什么时候用到CAS
### 并发容器
JUC包中大量使用CAS技术，既能保证安全性，又不需要是用互斥锁，大大提升工具类的性能
* ConcurrentHashMap

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
    //以下部分省略
    ...
}

//U代表Unsafe类型
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                    Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}

```
Unsafe 类包含 compareAndSwapInt、compareAndSwapLong、compareAndSwapObject 等和 CAS 密切相关的 native 层的方法，其底层正是利用 CPU 对 CAS 指令的支持实现的。

* ConcurrentLinkedQueue
非阻塞并发队列的offer也有CAS身影
```java
public boolean offer(E e) {
    checkNotNull(e);
    final Node<E> newNode = new Node<E>(e);

    for (Node<E> t = tail, p = t;;) {
        Node<E> q = p.next;
        if (q == null) {
            if (p.casNext(null, newNode)) {
                if (p != t) 
                    casTail(t, newNode); 
                return true;
            }
        }
        else if (p == q)
            p = (t != (t = tail)) ? t : head;
        else
            p = (p != t && t != (t = tail)) ? t : q;
    }
}

```
可以看出，在 offer 方法中，有一个 for 循环，这是一个死循环，在第 8 行有一个与 CAS 相关的方法，是 casNext 方法，用于更新节点。那么如果执行 p 的 casNext 方法失败的话，casNext 会返回 false，那么显然代码会继续在 for 循环中进行下一次的尝试。所以在这里也可以很明显的看出 ConcurrentLinkedQueue 的 offer 方法使用到了 CAS

### 数据库
MVCC乐观锁，利用version字段在数据库中实现乐观锁和CAS操作。
具体思路如下：当我们获取完数据，并计算完毕，准备更新数据时，会检查现在的版本号与之前获取数据时的版本号是否一致，如果一致就说明在计算期间数据没有被更新过，可以直接更新本次数据；如果版本号不一致，则说明计算期间已经有其他线程修改过这个数据了，那就可以选择重新获取数据，重新计算，然后再次尝试更新数据

### 原子类
AtomicInteger 使用了CAS，getAndAdd方法
```java
public final int getAndAdd(int delta) {    
    return unsafe.getAndAddInt(this, valueOffset, delta);
}

public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
    return var5;
}

```
重点看看compareAndSwapInt(),传入多个参数var1、var2、var5、var5+var4 分别代表：Object、offset、expectValue、newValue;  
* 第一个参数Object就是将要修改的对象，传入的就是this,是AtomicInteger对象本身
* offset，偏移量，借助它可以获取到value的数值
* expectvalue，代表期望值，传入的是刚才获取的var5,
* newvalue是希望修改为的新值，等于之前的数值var5,加上var4,var4就是我们之前所传入的delta，delta就是我们希望原子类所改变的数值。

Unsafe的getAndAddInt方法时通过<strong>循环+CAS</strong>方法来实现的。

## CAS有什么问题

### ABA问题
* CAS swap的标准是：当前的值和预期的值是否一致，如果一致，就认为在此期间这个数值没有发生变动，  
然而有些场景下，这个值从A变成了B，再由B变成A，它变化了两次，而CAS认为没有发生变成，检测不出此值是否被修改过。  
* 加一个版本号来解决ABA问题；atomic包中提供了<strong>AtomicStampedReference</strong>这个类专门用来解决ABA问题。解决就是利用版本号，会维护一种类似<Object, int>的数据结构，int用来计数，就是版本号

### 自旋时间过长
单词CAS不一定执行成功，CAS往往配合循环实现，有时候死循环不断重试，  
有可能场景本身就是高并发的场景，导致CAS一直不成功，循环时间越来越长，CPU资源一直被耗着。
### 范围不能灵活控制
只能保证一个共享变量的原子操作，多个变量共享操作是，这个时候可以用锁；取巧的办法是利用一个新的类，来整合这些共享变量，这个新类的多个成员变量就是刚才的多个共享变量，利用atomic包中的AtomicReference来把这个新对象整体进行CAS操作，这样保证线程安全。


