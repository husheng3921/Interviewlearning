# 有一亿个keys要统计，应该用哪种集合

<strong>四种统计模式：聚合统计、排序统计、二值状态统计和基数统计</strong>
## 聚合统计

聚合统计指多个集合元素的聚合结果，包括：统计多个集合的共有元素；把两个集合相比，统计其中一个集合独有的元素（差集统计）统计多个集合的所有的元素（并集统计）
* 统计手机APP每天的新增用户数和第二天的留存用户数，正好对应聚合统计
  
可以用一个集合记录所有登录过APP的用户ID，同时，用另一个集合记录每天登录过APP的用户ID.然后再做聚合统计。
1、使用Set类型，key设置为user:id,记录用户ID，value就是一个set集合，累计用户Set
2、每日用户Set，记录每天登录的用户ID，key-user:id:20200803,value记录用户ID
3、计算累计用户Set与user:id:20200803Set的并集结构，存在user:id中累计用户set中
```
SUNIONSTORE user:id user:id user:id:20200803
```
4、记录8月4号的用户，user:id:20200804Set中，执行SDIFFSTORE命令计算累计用户和0804Set 的差集，结果保存咋user:new的Set中
```
SDIFFSTORE user:new user:id:20200804 user:id
```
差集中的用户ID在0804Set中，不在累计用户Set中，所有user:new这个Set记录就是0804的新增用户
5、0804日的留存用户，计算0803Set与0804Set交集
```
SINTERSTORE user:id:rem user:id:20200803 user:id:20200804
```
> Set的差集、并集、交集的计算复杂度较高，在数据量较大的情况下，如果直接计算，会导致Redis实例阻塞；<strong>可以从集群中选择一个从库，让它专门负责聚合计算或者读取数据到客户端，客户端完成聚合统计</strong>

## 排序统计
提供最新评论列表的场景为例，
最新评论列表包含了所有评论中的最新留言，这要求集合类型能对元素排序，有序集合
常用集合（List，Hash，Set，Sorted Set）,List和Sorted Set就属于有序集合
* List是按照元素进入List的顺序进行排序的，而Sorted Set可以根据元素的权重来排序，我们可以自己来决定每个元素的权重值。

List的情况：
按照评论时间，每个新评论，就用LPUSH命令把它插入List的对头，一般会涉及分页操作；
```
LRANGE product 0 2
A
B
C

LRNAGE product 3 5
D
E
F
```
在展示第二页钱，又产生了一个新评论G，评论G就会被LPUSH命令插入到评论List的对头，变成了GABCDEF，再用刚才的命令获取第二页评论时，就会发现，评论C又被展示出来。

Sorted Set就不会存在这个问题，因为根据元素的实际权重来排序和获取数据。
按照评论时间的先后给每条评论设置一个权重值，然后把评论保存到Sorted Set中，ZRANGEBYSOCRE命令就可以按权重排序后返回元素。这样的话，即使集合中的元素频繁更新，Sorted Set也能通过ZRANGEBYSCORE命令准确地获取到按序排列的数据。
```

ZRANGEBYSCORE comments N-9 N
```

## 二值状态统计
二值状态统计就是指集合元素的取值只有0和1两种，
在签到统计时，每个用户一天的签到用1个bit位表示，选择Bitmap，redis提供的扩展数据类型；底层是用String类型作为底层数据结构实现的一种统计二值状态的数据类型，String类型是会保存为二进制的字节数组，所以Redis就把字节数组的每个bit位利用起来，用来表示一个元素的二值状态。
Bitmap提供了GETBIT/SETBIT操作，使用一个偏移量offset对bit数组进行读写，偏移量是从0开始，提供了BITCOUNT操作，统计bit数组中所有1的个数

1、记录用户0803签到，0开始偏移
```

SETBIT uid:sign:3000:202008 2 1 
```
2、检查该用户0803是否签到
```

GETBIT uid:sign:3000:202008 2 
```
3、统计用户在8月份的签到次数
```

BITCOUNT uid:sign:3000:202008
```

## 基数统计

基数统计是指统计一个集合中不重复的元素个数。对应统计网页的UV，统计需要去重，一个用户一天内的多次访问只能算作一次。Set默认支持去重
```
SADD page1:uv user1
```
用户1再次访问时，Set的去重功能就保证了不会重复记录用户1的访问次数；统计UV时，直接使用SCARD命令，会返回一个集合中的元素个数。

如果page1非常火爆，UV达到了千万，一个Set记录千万用户ID，会消耗很大的内存，
也可以用Hash，HSET page1:uv user1 1,多次访问也只会记录为1，统计HLEN命令统计Hash集合中的所有元素个数。同样也消耗很大的内存空间。

<strong>HyperLoglog</strong>
HyperLogLog是一种用于统计基数的数据集合类型，它的最大优势在于，集合元素非常多的时候，它计算基数所需的空间总是固定的，而且还很小。

每个HyperLogLog只需要花费12KB内存，计算接近2^64个元素的基数，与Set和Hash相比，非常节省空间。
统计UV时，可以用PFADD命令，把访问页面的每个用户添加到HyperLogLog中。
```

PFADD page1:uv user1 user2 user3 user4 user5
```
可以用PFCOUNT命令直接获得page1的UV值，返回HyperLogLog统计结果
```
PFCOUNT page1:uv
```
<strong>HyperLogLog的统计规则是基于概率完成的，所以给它出的统计结果是有一定误差的，标准误算率0.81%。如果要精确统计用Hash或Set</strong>

![](./../img/redis统计.jpg)



