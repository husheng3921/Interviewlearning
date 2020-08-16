# Spring 面试题汇总

## Spring如何解决循环依赖问题？

Spring采用“三级缓存”来解决循环依赖的问题，在Spring中定义了三个Map，来作为缓存：
* 一级缓存，singletonObjects,存放的已经实例化好的单例对象
* 二级缓存，earlySingletonObjects,存放的是还没组装好完毕提前曝光的对象
* 三级缓存，singleFactories,存放的是即将要被实例化的对象的对象工厂。

当我们需要创建一个bean时，首先会从一级缓存singletonObjects中去尝试获取这个bean；如果没有，则会尝试去二级earlySingleObjects获取；如果也没有，则会从三级缓存中获取，找到对应的工厂，获取未完全填充的bean，然后删除三级缓存，并将这个bean填充到二级缓存。