# java注解

## 什么是注解
在Spring2.0及早期时代，web项目开发是通过配置文件xml来实现bean的依赖注入；后来spring3.0时代，可以使用spring提供的java注解来取代xml配置。  
自从java5.0版本引入注解后，常见的注解@Override、@Deprecated。用于对代码进行说明，可以对包、类、接口、字段、方法参数、局部变量等进行注解，注解可以用个词描述<strong>元数据，一种数据的描述</strong>.注解就是源代码的元数据。  

## 注解分类
* @Documented注解是否将包含在javadoc中，用于描述其他类型的annotation应该被作为被标注的程序员公共API，因此可以被例如javadoc此类的工具文档化。Document是一个标记注解，没有成员
* @Retention注解的生命周期，RetentionPolicy.SOURCE(编译结束后失效如@Override)、RententionPolicy.CLASS(JVM加载类的时候失效，默认，不能通过反射获取)、RetentionPolicy.RUNTIME(始终不失效，一般用于定义注解，可以通过反射获取)
* @Target注解用在什么地方，是Annotation所修饰的对象范围，分别有几个地方
> ElementType.Type： 类上   
> ElementType.FIELD: 成员变量   
> ElementType.METHOD: 方法   
> ElementType.PARAMETER: 参数   
> ElementType.CONSTRUCTOR: 构造方法  
> ElementType.LOCAL_VARIABLE：本地变量   
> ElementType.ANNOTATION_TYPE: 另一个注释   
> ElementType.PACKAGE: 包上

* @Inherited注解作用被子类继承
  
## 注解的定义
* XML和注解的区别：
> 注解： 是一种分散式的元数据，与源代码紧绑定  
> xml: 是一种集中式的元数据，与源代码无绑定   

## 如何自定义注解  

### 必要步骤
* @Target必须有 
* 生命周期尽量RUNTIME
* @Interface声明
* 内部只支持基本类型、String类型、枚举类型
* 所有属性都必须写成field(),并可提供默认值default

### 如何使用注解
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Company {
   public enum Status {RUNNING, CLOSED}
   String name() default "NETEASE"； //有默认值
   Status status() default Status.RIUNNING;
   String location（）；//没有默认值
} 
public class Demo{
    @Company（name="阿里巴巴"，status=Company.Status.RUNNING,location="北京"）
    public void companyInfo（）{}
}
@interface Country{
  String value（）；
}
@Country（"中国"）
public void method(){};
```
### 注解标注完如何处理
定义一个注解处理类和注解处理方法；如果你熟悉反射代码，<font color="red">就会知道反射可以获取类名、方法和实例变量对象。所有这些对象getAnnotation()这个方法用来返回修饰他们的注解信息。</font>
```java

import java.lang.reflect.Field;
public class AnnotationProccessor {
    public  static void process(Demo demo){
        Class demoClazz = Demo.class;
          for(Method method : demoClazz.getMethods()) {
             Company companyAnnotation = (Company)method.getAnnotation(Company.class);
             if(companyAnnotation !=null) {
                System.out.println(" Method Name : "+ method.getName());
                System.out.println(" name : "+ companyAnnotation.name());
                System.out.println(" Status : "+ companyAnnotation.status());
     }
  } 
```