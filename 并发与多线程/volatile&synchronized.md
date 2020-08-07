# volatile & synchronized

## volatile是什么
volatile是一个java关键字，同步机制；当共享变量被volatile修饰，修改这个变量的值后，再读取时，保证获取最新值。  
相比于synchronized或者Lock，。volatile是更轻量的，volatile不会发生上下文切换等，不会让线程阻塞，用来保证线程安全，有限场景发挥。

## volatile适用场景
* 不适合 a++（原子操作)
* 适用场合：布尔标记位
* 适用场合：作为触发器

## volatile作用
* 保证可见性。
Happens-before关系中，对一个volatile写操作hanppen-before后面对该变量的读操作。
* 禁止重排序
as-if-serial：不管怎么重排序，单线程执行结果不会变。由于编译器或CPU优化，代码的实际执行顺序可能与我们编写的顺序不同，多线程中，乱序会导致严重的线程安全问题。

## volatile与synchronized

相似性：volatile可以看作一个轻量版的synchronized，比如一个变量如果自始至终被各个线程赋值和读取，没有其他操作，可以用volatile代替synchronized或者代替原子变量，足以保证线程安全。
不可替代：volatile不能替代synchronized，volatile没有提供原子性和互斥性
性能方面：volatile属性的读写都是无锁，高性能，比synchronized好。

