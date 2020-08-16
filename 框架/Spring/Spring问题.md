# Spring 面试题汇总

## Spring如何解决循环依赖问题？

Spring采用“三级缓存”来解决循环依赖的问题，在Spring中定义了三个Map，来作为缓存：
* 一级缓存，singletonObjects,存放的已经实例化好的单例对象,用于保存beanName和创建bean实例之间的关系，beanname-->bean instance
* 二级缓存，earlySingletonObjects,存放的是还没组装好完毕提前曝光的对象
* 三级缓存，singleFactories,存放的是即将要被实例化的对象的对象工厂。用于保存BeanName与创建的bean工厂之间的关系，beanname-->ObjectFactory

当我们需要创建一个bean时，首先会从一级缓存singletonObjects中去尝试获取这个bean；如果没有，则会尝试去二级earlySingleObjects获取；如果也没有，则会从三级缓存中获取，找到对应的工厂，获取未完全填充的bean，然后删除三级缓存，并将这个bean填充到二级缓存。


## BeanFactory和FactoryBean

* BeanFactory是Spring比较原始的Factory。如XMLBeanFactory典型的BeanFactory；无法支持AOP等功能
* ApplicationContext接口，它由BeanFactory接口派生而来，ApplicationContext包含BeanFactory的所有功能，

区别：
* BeanFactory是接口，提供IOC最基本的形式，
* FactoryBean也是接口，为IOC容器中的实现提供了更加灵活的方式，FactoryBean在IOC容器的基础上给Bean的实现加上了一个简单工厂和装饰器模式。我们可以通过getObject方法中灵活配置。
* BeanFactory是个工厂，IOC容器或对象工厂，FactoryBean是个Bean。所有Bean都由BeanFactory来进行管理，对FactoryBean而言，是个能生产或者修饰对象生成的工厂Bean，类似于工厂模式和装饰器模式。

### BeanFactory
BeanFactory是一个工厂类，它负责生产和管理bean的一个工厂。是IOC的核心接口，具体实现有DefaultListableBeanFactory、XMLBeanFactory、ApplicationContext等
ApplicationContext包含了BeanFactory所有功能，还提供了：
* MessageSource，提供国际化的消息访问
* 资源访问，如URL文件
* 事件传播
* 载入多个上下文

### FactoryBean
一般情况下，Spring通过反射机制利用bean的class属性来指定实现类实例化bean，在某些情况下比较复杂，按照传统方式，需要在`<bean>`中提供大量的配置信息，配置方式的灵活性受限，这时采用编码的方式可能得到一个简单的方案。Spring为此提供了一个org.springframework.bean.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化bean的逻辑。

以bean结尾，表示它是一个bean，
```java
public interface FactoryBean<T>{
    T getObject() throws Exception;
    Class<?> getObjectType();
    boolean isSingleton();
}
```
T getObject()：返回由FactoryBean创建的bean的实例，
通过getBean()返回的不是FactoryBean，而是FactoryBean#getObject()方法所返回的对象，相当于Factory#getObject()代理了getBean()
如果要获取FactoryBean对象，则在id前面加一个&符号来获取。
```java
<bean id="car" class="com.hs.factoryBean.CarFactoryBean" carInfo="超级跑车,400, 2000000"/>
```
`getBean("car")`，通过反射机制发现FactoryBean实现了FactoryBean的接口，容器就调用接口方法CarFactoryBean#getObject()方法返回。
如果希望获取CarFactoryBean的实例，通过`getBean("&car")`;
