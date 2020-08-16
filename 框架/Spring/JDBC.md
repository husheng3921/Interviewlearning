# JDBC

## JDBC数据库连接流程

* 在开发环境下指定数据库的驱动程序
* 在java程序中加载驱动程序，class.forName("com.mysql.jdbc.Driver")
* 创建数据连接对象，通过DriverManager类创建数据库连接对象Connection
* 创建Statement对象，主要用于执行静态SQL语句并返回生成的结果的对象
* 调用Statement对象的相关方法执行对应的SQL语句，通过execuUpdate()方法对数据更新，包括插入和删除；
* 通过调用executeQuery()方法进行数据的查询，查询结果会得到ResultSet对象。
* 关闭数据库连接池

## PreparedStatement
* PreparedStatement实例包含了已编译的SQL语句，包含于preparedStatement对象中的SQL语句中具有一个或多个IN参数，IN参数的值在SQL语句时未被指定，用“？”占位符，在执行前通过setXXX方法来提供。
* 由于PreparedStatement对象已经编译过，执行速度要快于Statement对象。

