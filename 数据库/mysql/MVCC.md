# MVCC(multi-version Concurrency Control)多版本并发控制

### 解决什么问题
* 锁机制可以控制并发操作，但其系统开销较大，而MVCC可以在大多数情况下代替行级锁，降低系统开销

### MVCC实现
MVCC通过保存数据在某个时刻的快照来实现，不同存储引擎的MVCC实现是不同的，典型的有乐观并发控制和悲观并发控制。

## MVCC具体实现分析
以Innodb的MVCC分析并发控制  
通过每行记录后保存两列隐藏的列来实现，分别保存行的创建时间，删除时间，<strong>这里的时间不是时间的时间值，而是系统版本号(理解事务的ID号),没开始一个新事务，系统版本号自增，事务开始时的系统版本号会作为事务的ID号</strong>，下面以读提交隔离级别下的MVCC。
### Insert
Innodb新插入一行保存当前系统版本号作为版本号，   
<strong>第一个事务ID为1；</strong>  
```sql
start transaction;
insert into yang values(NULL,'yang') ;
insert into yang values(NULL,'long');
insert into yang values(NULL,'fei');
commit;
```
对应数据在表中如表：
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|undefined|
|2|long|1|undefined|
|3|fei|1|undefined|

### Select *
> Innodb会根据下面两个条件检查记录:  
> * a.InnoDB只会查找版本早于当前事务版本的数据行(也就是,行的系统版本号小于或等于事务的系统版本号)，这样可以确保事务读取的行，要么是在事务开始前已经存在的，要么是事务自身插入或者修改过的.
> * b.行的删除版本要么未定义,要么大于当前事务版本号,这可以确保事务读取到的行，在事务开始之前未被删除.  
只有a,b同时满足才返回作为查询结果。  


<strong>第二个事务ID为2；</strong>
```sql
start transaction;
select * from yang;  //(1)
select * from yang;  //(2)
commit; 
```
假设1  
假设在执行这个事务ID-2的过程中，刚执行到(1)，这时另一个事务ID-3往表里插入了一条数据；  
<strong>事务ID-3</strong>
```sql
start transaction;
insert into yang values(NULL,'tian');
commit;
```
这时候表中的数据： 
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|undefined|
|2|long|1|undefined|
|3|fei|1|undefined|
|4|tian|3|undefined|

然后继续执行(2)，由于id=4的数据创建时间(事务ID-3)当前执行事务ID-2，而Innodb只会查找事务ID小于等于当前事务ID的记录。所以id=4的数据不会被执行事务ID-2中的(2)步骤检索出来，在事务ID-2中两条select都是下表:  
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|undefined|
|2|long|1|undefined|
|3|fei|1|undefined|

### Delete *
> InnoDB会为删除的每一行保存当前系统的版本号(事务的ID)作为删除标识.  

假设2  
假设在执行这个事务ID-2的过程中,刚执行到(1),假设事务执行完事务ID-3后，接着又执行了事务ID-4;
<strong>第四个事务ID-4</strong>:
```sql
start   transaction;  
delete from yang where id=1;
commit;  
```
这时数据库表
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|4|
|2|long|1|undefined|
|3|fei|1|undefined|
|4|tian|3|undefined|

执行事务ID-2中的(2)时，检索条件：创建时间小于等于当前时间(事务ID)和删除时间(事务ID)大于当前执行事务ID；而id=4上面已经说过，id=1的行删除时间大于当前事务ID所以会被检索出来。事务ID-2中两条select结果是：  
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|4|
|2|long|1|undefined|
|3|fei|1|undefined|

### Update *
> InnoDB执行UPDATE，实际上是新插入了一行记录，并保存其创建时间为当前事务的ID，同时保存当前事务ID到要UPDATE的行的删除时间.

假设3  
假设在执行完事务2的(1)后又执行,其它用户执行了事务3,4,这时，又有一个用户对这张表执行了UPDATE操作:   
<strong>第5个事务ID-5</strong>:
```sql
start  transaction;
update yang set name='Long' where id=2;
commit;
```
根据update规则，会生成新的一行，并修改原来要修改的列的删除时间列上添上本事务ID  
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|4|
|2|long|1|5|
|3|fei|1|undefined|
|2|Long|5|undefined|
继续执行事务ID-2的(2),根据select语句检索条件：
|id|name|创建时间(事务ID)|删除时间(事务ID)|
|--|--|--|--|
|1|yang|1|4|
|2|long|1|5|
|3|fei|1|undefined|


## 小结
* MVCC手段只适用于隔离级别中的Read Commited(读提交),Repeatable Read(可重复读)  
* Read uncommitted,读未提交，存在脏读，能读到未提交的数据，MVCC的版本创建和删除版本只有在事物提交后才会产生。
* 串行化会对所涉及的表加锁，并非行锁，自然不存行的版本控制问题。
* 综上，MVCC主要作用于事务性的，有行锁控制的数据库模型。

### 其他参考阅读
[MySQL事务隔离级别和MVCC](https://juejin.im/post/5c9b1b7df265da60e21c0b57)