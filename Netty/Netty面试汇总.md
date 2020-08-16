
# Netty面试汇总

## Netty是什么

* Netty是一款基于NIO（Nonblocking I/O)客户端-服务端的框架，快速简单的开发网络应用程序。  
* 极大的简化并优化TCP和UDP套接字服务器等网络编程，并且性能以及安全性等很多方面甚至都要好。
* 支持多种协议：FTP、HTTP以及各种二进制和基于文本的协议

## Netty的特点

* 高并发：基于NIO开发的网络通信框架
* 传输快：Netty的传输实现了零拷贝特性，尽量减少不必要的内存拷贝，实现了高效率的传输。
* 封装好：Netty封装了NIO操作的很多细节，提供易于调用接口

## 什么是零拷贝

Netty的零拷贝主要包含三个方面：
* Netty的接收和发送ByteBuffer采用DIRECTBUFFERS，使用堆外内存进行Socket读写，不需要进行字节缓冲去的二次拷贝。使用传统的堆内存（HEAP BUFFERS）进行socket读写，JVM会将堆内存的buffer拷贝一份到直接内存中，然后才写入socket中。相比于堆外直接内存，消息在发送过程中多了一次缓冲去内存拷贝。
* Netty提供了组合Buffer对象，可以聚合多个ByteBuffer对象，用户可以像操作一个Buffer那样方便的组合buffer进行操作，避免了传统通过内存拷贝的方式将几个小的buffer合并成一个大的buffer。
* Netty的文件传输采用transferTo方法，它可以直接将文件缓冲区的数据发送到目标Channel，避免了传统通过循环write方式导致内存拷贝问题。

## Netty的优势有哪些

* 使用简单：封装了NIO的很多细节，使用简单
* 功能强大：预置了多种编解码功能，支持多种主流协议
* 定制功能强：可以通过ChannelHandler对通信框架进行灵活地扩展
* 性能高：通过与其他业界主流的NIO框架对比，Netty的综合性能最优
* 稳定：Netty修复了已经发现的所有NIO的bug，让开发人员专注于业务网本身
* 社区活跃：Netty是活跃的开源项目，版本迭代周期短

## Netty使用场景
 * 阿里分布式服务框架Dubbo,默认使用Netty作为基础通信框架
 * RocketMQ也是使用Netty作为通讯的基础

## Netty高性能表现在哪些方面

* IO线程模型：同步非阻塞，用最少的资源做更多的事
* 内存零拷贝：尽量减少不必要的内存拷贝，实现了更高效率的传输
* 内存池设计：申请的内存可以重用，主要直接内存，内部实现用一颗二叉查找树管理内存分配情况。
* 串行化读写：避免使用锁带来的性能开销
* 高性能序列化协议：支持protobuf等高性能序列化协议

## 什么是TCP粘包和拆包

是指基于TCP发送数据时候，出现多个字符串“粘”在一起或者被拆开的问题。
解决：
* 使用自带的解码器
  * LineBaseFrameDecoder：发送端发送数据包的时候，每个数据包以换行符作为分隔符；LineBaseFrameDecoder工作原理依次遍历ByteBuf中的可读字节，是否有换行符，然后进行读取。  
  * DelimiterBasedFrameDecoder:自定义分割符解码器
  * FixedLengthFrameDecoder：固定长度解码器，按照固定的长度对消息进行相应的拆包

* 自定义序列化编解码器
  * java自带的序列化Serializable接口实现序列化，性能和安全问题一般不会被使用
  * Protobuf、Hessian2、json、Thrift等

## Netty长连接、心跳机制

### TCP短连接和长连接

TCP的三次握手建立连接，四次挥手关闭连接，比较消耗网络资源并且耗时。  
* 短连接：指server与Client建立连接之后，完成读写之后就关闭连接，如果下一次要在发送消息，就要重新连接。
* 长连接：建立连接后，完成一次读写，不主动关闭，后续读写操作继续基于这个连接；省去较多的TCP建立和关闭操作，降低对网络资源的依赖，节约时间。

### 为什么需要心跳机制？Netty中心跳机制了解吗

在保持TCP长连接的过程中，可能会出现断网等网络异常情况，异常发生的时候，Client与server没有交互的话，无法发现对方已经掉线，需要引入心跳机制。TCP本身自带的长连接选项，也有心跳包机制，TCP的选项：SO_KEEPALIVE；但是不够灵活。
Netty层通过编码实现心跳机制，核心类是IdleStateHandler,
* readerIdleTime:为读超时时间(测试端一定时间内未接收到被测试端消息)
* writerIdleTime：为写超时时间（测试端一定时间内向被测试端发送消息）
* allIdleTime： 所有类型的超时时间。

## Netty中有哪些重要组件
* Channel：Netty网络操作抽象类，它出了包括基本的I/O操作，如bind、connect、read、write等
  > Channle接口实现类：NIOServerSocketChannel(服务端)和NIOSocketChannel客户端，对应BIO编程模型中的ServerSocket以及Socket两个概念。
* EventLoop：主要是配合Channel处理I/O操作，用来处理生命周期中所发生的的事情，<strong>主要实际作用就是负责监听网络实践并调用事件处理器进行相关I/O操作的处理。
* ChannelFuture：Netty框架中所有的I/O操作都为异步的，因为我们需要ChannelFuture的addListener()注册一个ChannelFutrueListener监听事件，当操作执行成功或失败时，监听就会自动触发返回结果。
* ChannelHandler：充当所有处理入站和出站的逻辑容器。ChannelHandler主要用来处理各种事件，这里事件包括：连接、数据接收、异常、数据转换等
* ChannelPipline：为channelHandler链提供了容器，当Channel创建时，就会被自动分配到它专属的ChannelPipeline，这个是永久关联的，ChannelPipeline上通过AddLast()方法添加一个或多个ChannelHandler，因为一个数据或事件被多个Handler处理，当一个ChannelHandler处理完之后将数据交给下一个ChannelHandler。


