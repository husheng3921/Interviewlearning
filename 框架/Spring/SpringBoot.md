# SpringBoot

## SpringBoot核心功能
* 独立运行Spring项目，java -jar xx.jar
* 内嵌Servlet容器：tomcat，jetty
* 提供Starter简化Maven配置
* 自动配置Spring bean：springboot检测到特定的类的存在，就会针对这个应用做一定的配置
* 准生产的应用监控
* 无代码生成和XML配置


## SprigBoot核心注解

```java
@SpringBootApplication

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    Class<?>[] exclude() default {};

    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    String[] excludeName() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};
}
```
* @Configuration注解，指定类是Bean定义的配置类，来自Spring-context
* @ComponentScan注解，扫描指定包下的beans,来自Spring-context
* @EnableAutoConfiguration注解，打开自动配置功能。如果我们要关闭某个自动配置，可以设置注解的exclude或excludeName属性。来自springboot新特性

## SpringBoot自动装配原理

[自动装配](https://juejin.im/post/6844904137746808839#heading-4)
[自动装配原理](https://juejin.im/post/6844903840307740686)
* 使用@EnableAutoConfiguration注解，打开spring boot自动配置的功能
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
```
* Springboot在启动时扫描项目所在的依赖的jar包，寻找包含Spring.factories文件的jar包
* 根据Spring.factories配置加载AutoConfigure类
* 根据@Conditional等条件注解的条件，进行自动配置并将bean注入IOC容器中。

## Springboot 常用有哪些starter
* web
* aop
* test
* data-redis

## starter是什么

## SpringBoot启动源码分析

从`SpringApplicatio.run(Application.class, args)`方法开始
```java
public ConfigurableApplicationContext run(String... args) {
    // 1.创建并启动计时监控类
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    // 2.声明应用上下文对象和异常报告集合
    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList();
    // 3.设置系统属性 headless 的值
    this.configureHeadlessProperty();
    // 4.创建所有 Spring 运行监听器并发布应用启动事件
    SpringApplicationRunListeners listeners = this.getRunListeners(args);
    listeners.starting();
    Collection exceptionReporters;
    try {
        // 5.处理 args 参数
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        // 6.准备环境
        ConfigurableEnvironment environment = this.prepareEnvironment(listeners, applicationArguments);
        this.configureIgnoreBeanInfo(environment);
        // 7.创建 Banner 的打印类
        Banner printedBanner = this.printBanner(environment);
        // 8.创建应用上下文
        context = this.createApplicationContext();
        // 9.实例化异常报告器
        exceptionReporters = this.getSpringFactoriesInstances(SpringBootExceptionReporter.class, new Class[]{ConfigurableApplicationContext.class}, context);
        // 10.准备应用上下文
        this.prepareContext(context, environment, listeners, applicationArguments, printedBanner);
        // 11.刷新应用上下文
        this.refreshContext(context);
        // 12.应用上下文刷新之后的事件的处理
        this.afterRefresh(context, applicationArguments);
        // 13.停止计时监控类
        stopWatch.stop();
        // 14.输出日志记录执行主类名、时间信息
        if (this.logStartupInfo) {
            (new StartupInfoLogger(this.mainApplicationClass)).logStarted(this.getApplicationLog(), stopWatch);
        }
        // 15.发布应用上下文启动完成事件
        listeners.started(context);
        // 16.执行所有 Runner 运行器
        this.callRunners(context, applicationArguments);
    } catch (Throwable var10) {
        this.handleRunFailure(context, var10, exceptionReporters, listeners);
        throw new IllegalStateException(var10);
    }
    try {
        // 17.发布应用上下文就绪事件
        listeners.running(context);
        // 18.返回应用上下文对象
        return context;
    } catch (Throwable var9) {
        this.handleRunFailure(context, var9, exceptionReporters, (SpringApplicationRunListeners)null);
        throw new IllegalStateException(var9);
    }
}
```
### springboot 启动流程
* 1创建并启动你那个计时监控类
  此计时器是为了监控并记录Spring Boot应用启动的时间的，它会记录当前任务的名称，然后开始计时器。
* 2声明应用上下文对象和异常报告集合
  此过程声明了应用上下文对象和一个异常报告的ArrayList集合
* 3设置系统属性headless的值
  设置java.awt.headless=true，其中awt(Abstract Window Toolkit)的含义是抽象窗口工具集。设置爱为true表示运行一个headless服务器，可以用它来作一些简单的图像处理。
* 4创建所有Spring运行监听器并发布应用启动事件
  此过程用于获取配置的监听器名称并实例化所有的类
* 5初始化默认应用的参数类
  也就是说声明并创建一个应用参数对象
* 6准备环境
  创建配置并且绑定环境(通过property sources和profiles等配置文件)
* 7创建Banner的打印类
  SpringBoot启动时会打印Banner图片，此banner信息在SpringBootBanner类中定义的，我们可以通过实现Banner接口来定义banner信息，然后通过代码setBanner()方法设置SpringBoot项目使用自己自定义的Banner信息、
* 8创建应用上下文
  根据不同的应用类型创建不同的ApplicationContext上下文对象
* 9实例化异常报告器
  它调用的getSpringFactoriesInstances()方法类获取配置异常类的名称，并实例化所有的异常处理类
* 10准备应用上下文
  此方法的主要作用是把上面已经创建好的对象，传递给prepareContext来准备上下文，例如环境变量environment对象绑定到上下文中、配置bean生成器以及资源加载器、记录启动日志操作等
* 11刷新应用上下文
  此方法用于解析配置文件，加载bean对象，并且启动内置的web容器操作
* 12应用上下文刷新之后的事件处理
  这个方法的源码是空的，可以作一些自定义的后置处理操作
* 13停止计时监控类
  停止此过程第一步中的程序计时，并统计任务的执行信息。
* 14输出日志信息
  把相关的记录信息，如类名、时间等信息进行控制台输出
* 15发布应用上下文启动完成事件
  触发所有SpringApplicationRunListener监听器的started事件方法
* 16执行所有Runner运行器
  执行所有的ApplicationRunner和CommandLineRunner运行器
* 17发布应用上下文就绪事件
  触发所有的SpringApplicationRunListener监听器的running事件
* 18返回应用上下文对象
  
  
