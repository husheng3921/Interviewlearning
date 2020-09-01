# Linux命令

## Linux之grep "XXX" * | wc -l 命令

* 查看某文件下所有文件中的某个字符的个数
```shell
grep 'xx' * | wc -l

[root@izwz9853xzzbsd0y95igboz ~]# grep 'a' log.txt | wc -l
4
```
* 查看某文件夹下的个数，包括子文件夹里的

```shell
ls -l |grep "^-"|wc -l"
```
* wc

  * 命令格式 `wc -l`
  * 命令功能：统计指定文件中的字节数、字数、行数，并将结果输出
  * 命令参数：
    * -c:统计字节数
    * -l:统计行数
    * -m:统计字符数
    * 
```
wc -l
```
统计输出的信息的行数

## awk

```shell
awk [选项参数] 'script' var= value file(s)

awk [选项参数] -f scriptfile var=value file(s)
```

* -F fs：输入文件拆分隔符
* -v var=value:赋值用户定义一个变量
* -f scriptfile：从脚本文件中读取AWK命令

```shell
# 每行按空格或tab分割，输出文本的1、4项
[root@izwz9853xzzbsd0y95igboz ~]# awk '{print $1, $4}' log.txt
2 a
3 like
This's 
10 orange,apple,mongo
```
`awk -F # -F相当于内置FS，指定分割符`

```shell
# 使用分隔符“，”分割
[root@izwz9853xzzbsd0y95igboz ~]# awk -F, '{print $1, $4}' log.txt
2 this is a test 
3 Are you like awk 
This's a test 
10 There are orange 
```

### 运算符
[参考](https://www.runoob.com/linux/linux-comm-awk.html)
![](./img/awk.png)

```shell
# 输出第二列包含 "th"，并打印第二列与第四列
[root@izwz9853xzzbsd0y95igboz ~]# awk '$2 ~ /th/ {print $2,$4}' log.txt
this a

# 过滤第一列大于2的行
[root@izwz9853xzzbsd0y95igboz ~]# awk '$1>2' log.txt
3 Are you like awk
This's a test
10 There are orange,apple,mongo
# 过滤第一列大于2并且第二列等于'Are'的行
[root@izwz9853xzzbsd0y95igboz ~]# awk '$1>2 && $2=="Are"' log.txt
3 Are you like awk
[root@izwz9853xzzbsd0y95igboz ~]# awk '!/th/' log.txt
3 Are you like awk
This's a test
10 There are orange,apple,mongo
[root@izwz9853xzzbsd0y95igboz ~]# 
```