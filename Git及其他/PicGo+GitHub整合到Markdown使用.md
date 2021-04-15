# PicGo+GitHub图床，结合Markdown



## PicGo介绍

是一款图片上传的工具，目前支持微博图床、七牛图床、GitHub等图床。将本地的图片或剪切板上面的截图发送到图床，然后生成在线图片的链接。

![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205185328.png)

## 使用Gitee创建图床

https://blog.csdn.net/weixin_39963192/article/details/114931911

## 创建GitHub图床，注册自己的GitHub账号

1. 创建GitHub账号，登录

2. 创建Repository

   <img src="https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205185636.png" style="zoom:80%;" />

   填写Repository name、public、等信息

   3. 生成一个Token用于GitHub repository

      点击GitHub用户头像下->**settings->Developer settings**

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205185947.png)

      点击Developer settings后-> Personal access tokens  Generate new token

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205190144.png)

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205190347.png)

      填写信息后，点击Generate token，生成一个token，**要及时保存，后面不再会被看到**，

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205190625.png)

      ## 配置PicGo,

      ### 1.下载PicGo

      https://github.com/Molunerfinn/PicGo/releases

      选择合适自己电脑的版本下载安装。

      ### 2.配置图床，Windows为例

      打开PicGo，可能在右下角，选择打开详细窗口

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205191033.png)

      如图配置

      ```shell
      仓库名按照："账户名/仓库名"的格式
      分支名统一写："main",以前是master，现在改为main了
      将之前申请的token复制到设定token处
      存储路径可以指定存储路径，默认就写个 "/img"
      设定自定义域名，上传图片后，PicGo会将"自定义域名+上传图片名"生成一个可以访问的链接，
      https://raw.githubusercontent.com/用户名/RepositoryName/分支名  自己按照这个格式编写
      ```

      ### 3.快捷键设置编辑

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205191709.png)

      点击设置后

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205191814.png)

      我们每次截图后，同时按`Ctr+shift+C`就可以保存图片到GitHub图床了。会生成一个链接，点击复制即可

      ![](https://raw.githubusercontent.com/husheng3921/Figurebed/main/img/20210205191946.png)

      同时也可以修改图片链接

      ## 参考

      https://juejin.cn/post/6844903768782290957

      https://blog.csdn.net/weixin_39963192/article/details/114931911 (Gitee创建图床)

      
      
      