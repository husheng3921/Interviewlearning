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
* 根据Spring.factories配置价值AutoConfigure类
* 根据@Conditional等条件注解的条件，进行自动配置并将bean注入IOC容器中。

## Springboot 常用有哪些starter
* web
* aop
* test
* data-redis

## starter是什么
