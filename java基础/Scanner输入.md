
## Scanner类中next系列方法的总结

```java
next()
nextInt()
nextLine()
```
next系列的方法，他们的作用都是从键盘中接收数据。当程序执行到他们的时候，在命令中就开始等待键盘输入，结束标志都是'\n'也就是回车。

## next() 方法
next()不光是在接收键盘的内容，而且还是在进行扫描分割。比如next()默认的分隔符时空格。  

```java
Scanner sc = new Scanner(System.in);
while(true){
    String str = sc.next();
    System.out.println(str+'A');
}
```
输入 hjk kl lk
输出：
hjkA
klA
lkA

## nextLine()方法
nextLine()：读取输入，包括单词之间的空格和<strong>除去回车</strong>以外的所有符号。读取输入后，nextLine()将光标定位在下一行。
和next()区别就是它没有分割符，直接扫描全部输入内容，并创建对象将其引用返回。

## nextInt()方法
* nextInt()是取next()然后把字符串解析成一个int 字符。
* hasNextInt()是判断下次调用next()时是否可以得到一个安全解析成int的字符串，下一个next()返回值可以解析成为一个数字，即符合数字的格式，那么返回true。
* nextInt()和next类似，也有分割符
* 换行下坠

```java
Scanner sc = new Scanner(System.in);
int T = sc.nextInt();
for(int i = 0; i < T; i++){
    String str = sc.nextLine();
    System.out.println(str);
}
```
输入 3
hu
kl
kk

输出：

hu
kl
结束
输入3后，会直接输出换行符，我们可以消耗掉换行符
改造：
```java
Scanner sc = new Scanner(System.in);
int T = sc.nextInt();
sc.nextLine();
for(int i = 0; i < T; i++){
    String str = sc.nextLine();
    System.out.println(str);
}

```
或
```java
Scanner sc = new Scanner(System.in);
int T = sc.nextInt();
for(int i = 0; i < T; i++){
    String str = sc.next();//注意用next
    System.out.println(str);
}

```

## 小结
一般nextLine()在前面，nextInt()或next后，输入回车后输出不会出现逻辑问题。
但是next()或nextInt()在前面时，nextLine()会出现逻辑问题，需要sc.nextLine()消耗掉换行。