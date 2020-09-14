# MyBatis

## MyBatis常用对象
* SqlSessionFactory
SqlSessionFactory 对象可以通过SqlSessionFactoryBuilder对象获得，Builder可以从XML配置实例构建一个SqlSessionFactory，一旦创建，执行期间都存在，单例模式，
SqlSessionFactory构建SqlSession对象

* SqlSession
  SqlSession是MyBatis关键对象，执行持久化操作的对象，类似于JDBC中的Connection


## Spring与MyBatis整合

### sqlSessionFactory创建
spring通过org.mybatis.Spring.SqlSessionFactoryBean封装了MyBatis实现
* 实现了InitializingBean
* 实现了FactoryBean

sqlSessionFactory buildSqlSessionFactory()返回对象
支持多种属性配置：configLocation、typeAliasePackage、typeAliases、mapperLocations等。

获取SqlSessionFactoryBean，实现了FactoryBean，通过getBean获取对应实例，从而获取初始化后的sqlSessionFactory.

### MapperFactoryBean的创建

```java
UserMapper usermapper = sqlSession.getMapper(UserMapper.class);
```
mybatis根据配置信息，为UserMapper类型动态创建了代理类，而Spring
```java
UserMapper usermapper = (UserMapper)context.getBean("userMapper");
```
可以推断在bean的创建过程中，一定时使用了MyBatis中原生方法sqlSession.getMapper().
从而找到org.mybatis.Spring.mapper.MapperFactoryBean

同样实现InitializingBean和FactoryBean
MapperFactoryBean初始化时会检查sqlSession不为空的，映射文件存在性验证，映射接口验证，
* 获取MapperFactoryBean实例
通过getBean方法，获取该类的getObject函数返回实例。

### MapperScanConfigurer
通过配置，扫描特定的包，自动帮我们成批的创建映射器。




## 动态SQL

### if
```xml
<select id="select1">
    select * from user where name = 'hh'
    <!--可选条件-->
    <if test="id != null ">
        and id = #{id}
    </if>
</select>
```

### where
```xml

<select id="select1">
    select * from user where 
    <!--可选条件-->
    <if test="id != null ">
        id = #{id}
    </if>
</select>
```
如果id未传入则有错

```xml
<select id="select1">
    select * from user 
    <where>
    <!--可选条件-->
    <if test="id != null ">
        id = #{id}
    </if>
    <if test"age != null">
        and age = #{age}
    </if>
    </where>
</select>
```

### set
```xml
update user
<set>
  <if test="name != null"> name =#{name},</if>
...
</set>
```

### foreach
```xml
select * from user where id in
<foreach item="item" index="index" collection ="list" open =
 "(" separator="," close=")">
  #{item}
  </foreach>
```

插入where会自动剔除第一个条件前的and 或 Or

## MyBatis缓存机制
MyBatis提供了查询缓存来缓存数据，从而达到提高查询性能的要求。MyBatis的查询缓存分为一级缓存和二级缓存。
* 一级缓存是SqlSession级别的缓存
* 二级缓存是mapper级别，多个SqlSession共享的。

### 一级缓存SqlSession级别
* 操作数据库时需要构造SqlSession对象，在对象中有一个HashMap用于存储缓存的数据。不同SqlSession之间的缓存数据区域(HashMap)是互相不影响的。  
* 一级缓存是SqlSession范围的，同一个SqlSession中执行两次相同的SQL语句时，第一次执行完毕会将数据写到缓存，第二次查询时会直接从缓存中获取数据，不再底层数据库查询。但是执行DML（insert、Update、delete)提交到数据库后，MyBatis会清空SqlSession中的一级缓存。
* 默认开启一级缓存。

### 二级缓存Mapper级别

二级缓存是mapper级别的缓存，使用二级缓存时，多个SqlSession使用同一个Mapper的sql语句去操作数据库，得到数据会在二级缓存区域，它同样使用HashMap进行数据存储。二级缓存比一级缓存的范围更大，多个SqlSession共享，作用域是mapper同一个namespace。
* 默认没有开启二级缓存，需要在setting全局参数中开启二级缓存配置

参考配置
```xml
<settings>
    <setting name="cacheEnabled" value ="true"/>
</settings>
```
在当前mapper.xml下开启配置
```xml
<cache eviction="LRU" flushInterval="60000" size="512" readOnly="true"/>
```

## 面试题
* MyBatis怎么防止SQL注入
  * `#`传入的数据会当成一个字符串，会对自动传入的数据加一个双引号。
  * `$`将传入的数据直接显示生成在SQL中
  * 预编译，底层用的JDBC中的PreparedStatement类在起作用，对象包含了编译好的SQL语句，执行时，`#{}`替换占位符就阔以了。
  * `${}`输出变量的值，