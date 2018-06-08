# ml-blog
ml-blog 是基于 Spring Boot + Mybatis 开发的一套开源博客系统。作者之前使用 hexo 搭建博客网址，但是没有后台管理界面，管理文章比较麻烦，因此 ml-blog 就诞生了。

演示站点：<https://www.extlight.com>

# 技术栈

## 后端

* 核心框架：Spring Boot
* 持久层框架：MyBatis
* 模板框架：Thymeleaf
* 数据库连接池：Alibaba Druid
* 服务端验证：Hibernate Validator
* 缓存框架：Ehcache 
* 验证码框架：Kaptcha、Geetest
* 搜索框架：Lucene
* Markdown ：Commonmark
* 工具类：Lombok、通用 Mapper、PageHelper...

## 前端

* JS框架：Jquery
* CSS框架：Bootstrap
* 富文本编辑器：editor.md
* 文件上传：dropzone
* 弹框：sweetalert
* 点击效果：wave
* 代码修饰：highlight
* 统计访问：不蒜子

# 第三方

* 七牛云（文件上传）
* Geetest（人机验证）
* 畅言（评论系统）
* Leancloud（统计）

# 预览效果

前端效果可通过上边的网址进行浏览，下边张贴后台管理的截图：

![](http://images.extlight.com/ml-blog-01.jpg)

![](http://images.extlight.com/ml-blog-02.jpg)

![](http://images.extlight.com/ml-blog-03.jpg)

![](http://images.extlight.com/ml-blog-04.jpg)

![](http://images.extlight.com/ml-blog-05.jpg)

![](http://images.extlight.com/ml-blog-06.jpg)

# 安装

下载源码，执行 resources 目录下的 ml-blog.sql 文件，然后 application.properties 中连接数据库的用户名、密码以及 lucene 索引生成路径，运行项目即可。

前端访问地址: <http://localhost:8080>

后台访问地址：<http://localhost:8080/admin> 用户名：admin 密码：123456

