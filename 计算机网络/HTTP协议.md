# HTTP



## 无状态理解 
[https://www.zhihu.com/tardis/sogou/qus/23202402](https://www.zhihu.com/tardis/sogou/qus/23202402)
* 协议的状态是指下次传输可以保留这次传输信息的能力。  
HTTP协议自身不具备保存之前发送过的请求或响应的功能。每次新的请求，有对应新的响应。  
* 保留状态的方法
>1、Cookie是客户端的解决方案：Cookie是服务端发送给客户端的一些消息(响应报文中，Set-Cookie字段，通知客户端保留cookie)，下次访问服务器时带上cookie，服务端接收到带cookie的请求，分析哪个客户端发送过来的。  
>2、session是服务端保持状态的方法：既指客户端与服务端一系列交互的动作，又指服务端为客户端开辟的存储空间；session的持续时间以浏览器关闭结束；通过键值的形式获取匹配内容。


## URI、URL

### URI(Unifrom Resource Identifier)
统一资源标识符，用来标识某一互联网资源，
![](/计算机网络/img/http-1.png)
### URL与URI
URL 统一资源位置(互联网上所处的位置)，URL是URI的子集。

## 状态码

### 状态码类别
| |类别|原因短语|
|--|--|--|
|1XX|Infromational(信息性状态码)|接收的请求正在处理|
|2XX|Success(成功状态码)|请求正在处理|
|3XX|Redirection(重定向)|需要进行附加操作完成请求|
|4XX|Client Error(客户端错误)|服务器无法处理请求|
|5XX|Server Error(服务器错误状态码)|服务器无法处理请求|

### 常见的状态码
* 2XX  
|状态码|意义|
|--|--|
|200|OK，返回成功|
|201|创建成功|
|204|No Content，请求成功，无资源返回,响应报文实体不含body部分|
|206|Partial Content，部分资源返回，响应报文Content—Range指定内容|

* 3XX  
|状态码|意义|
|--|--|
|301|Moved Permanently，永久性重定向，资源URI已更新|
|302|Found,资源的URI临时定位到其他位置|

* 4XX  
|状态码|意义|
|--|--|
|400|Bad Request,请求报文中存在语法错误|
|401|Unauthorized,未授权，需要认证|
|403|Forbidden，拒绝访问请求资源|
|404|Not Found,服务器上没有请求的资源|
|405|请求方法不对|

* 5XX  
|状态码|意义|
|--|--|
|500|Internal Server Error服务器内部错误|
|503|Service Unavailable，服务不可用|
