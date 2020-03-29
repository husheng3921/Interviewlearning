
# JVM内存模型

## JVM结构

## 元空间、堆、线程独占部分
```java
public class HelloWorld {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void sayHello(){
        System.out.println("hello" + name);
    }
    public static void main(String[] args) {
        int a = 1;
        HelloWorld hw = new HelloWorld();
        hw.setName("husheng");
        hw.sayHello();
    }
}
```
> 元空间
>> Class:HelloWorld-Method：sayHello、setName\getName\main-Field:name
>> Class:System  

> Java堆
>>Object: String("husheng")  
>>Object: HelloWorld

>线程独占
>>Parameter reference: "husheng" to String Object  
>>Variable reference: "hw" to HelloWorld object  
>>Local Variables: a with 1, lineNo

## 面试问题

* Java内存模型与JVM的区别？
* JVM内存模型中堆和栈内存分配策略：
> 静态存储：编译时确定每个数据目标在运行时的存储空间需求  
> 栈式存储：数据区需求在编译时未知，运行时模块入口前确定  
> 堆式存储：编译时或运行时模块入口都无法确定，动态分配  

* JVM内存中的堆栈区别
> 堆栈联系：引用对象、数组时，栈里定义变量保存堆中目标的首地址  
> 管理方式：栈自动释放，堆需要GC  
> 空间大小：栈比堆小  
> 碎片相关：栈产生的碎片远小于堆  
> 分配方式： 栈中支持静态和动态分配，而堆仅支持动态分配。 
> 效率： 栈的效率比堆高  

