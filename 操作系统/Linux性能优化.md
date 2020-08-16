# Linux性能优化

## CPU性能
### 平均负载

```shell
hadoop@husheng:~$ uptime
 10:54:30 up 66 days, 19:11,  5 users,  load average: 3.60, 4.83, 5.10
```
后面三个数字依次是过去1分钟、5分钟、15分钟的平均负载(Load Average)。

<strong>平均负载</strong>是指单位时间内，系统处于可运行状态和不可中断状态的平均进程数，也就是平均活跃进程数，它和CPU没有直接关系。
<strong>可运行</strong>正在使用CPU或正在等待CPU的进程，就是我们用ps命令看到的，处于R状态(Running)或(Runnable)的进程。
<strong>不可中断</strong>则是正处于内核关键流程中的进程，并且这些流程不可打断的，比方说常见的等待设备的I/O响应。

当平均负载是2，表示：
* 只有2个CPU的系统上，意味着全部完全被占用
* 只有1个CPU的系统删个， 意味着有一半的进程竞争不到CPU.

#### 平均负载多少合理

* 首先获取系统CPU个数
```shell
hadoop@husheng:~$ grep 'model name' /proc/cpuinfo | wc -l
4
```
三个不同时间间隔的平均值，提供了，分析系统负载趋势的数据。
在实际生产中，<strong>平均负载高于CPU数量70%</strong>排查负载高的问题。

### 平均负载与CPU使用率
平均负载指单位时间内，处于可运行和不可中断状态的进程数，不仅包括了正在使用CPU的进程，还包括等待CPU和等待IO进程。  

分析工具：
* mpstat一个多核CPU常用性能分析工具，用来实时查看每个CPU的性能指标，以及所有CPU的平均指标。
* pidstat是一个常用进程性能分析工具，用来实时查看进程的CPU、内存、I/O以及上下文切换等性能指标。

## CPU使用率查看与分析

查看CPU使用率常用top与ps
* top显示了系统总体的CPU和内存使用情况，以及各个进程的资源使用情况，默认3秒间隔。
* ps则只显示了每个进程的资源使用情况，整个进程生命周期。

```shell

# 默认每3秒刷新一次
$ top
top - 11:58:59 up 9 days, 22:47,  1 user,  load average: 0.03, 0.02, 0.00
Tasks: 123 total,   1 running,  72 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.3 us,  0.3 sy,  0.0 ni, 99.3 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  8169348 total,  5606884 free,   334640 used,  2227824 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  7497908 avail Mem

  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
    1 root      20   0   78088   9288   6696 S   0.0  0.1   0:16.83 systemd
    2 root      20   0       0      0      0 S   0.0  0.0   0:00.05 kthreadd
    4 root       0 -20       0      0      0 I   0.0  0.0   0:00.00 kworker/0:0H
...
```

