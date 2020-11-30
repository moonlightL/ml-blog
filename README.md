# ml-blog

ml-blog 是基于 Spring Boot + Mybatis + Mysql 开发的一套开源博客系统。作者之前使用 hexo 搭建博客网址，但是没有后台管理界面，管理文章比较麻烦，因此 ml-blog 就诞生了。

[![](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/moonlightL/ml-blog/blob/master/LICENSE)
![](https://img.shields.io/badge/language-Java-blue.svg)

**该项目已不再进行维护，最新博客系统请移步至 [Hexo Boot](https://github.com/moonlightL/hexo-boot)**

# 适用对象

* Spring Boot 初学者。该博客系统综合运用了作者发表的 《Spring Boot 入门》 系列的文章提及的知识内容，初学者可以阅读文章以及结合该项目学习。
* 与作者一样，使用 hexo 但苦于没有后台管理工具（界面）管理文章的写作者。该博客系统模仿 hexo 生成的访问路径，并支持 markdown 文件导入功能。
* 懵懂者。初次接触博客系统的人。

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
* 弹框插件：sweetalert
* 点击效果：wave
* 代码修饰：highlight
* 统计访问：不蒜子
* 分享插件：百度分享

## 第三方

* 七牛云（文件上传）
* Geetest（人机验证）
* 畅言（评论系统）
* Leancloud（统计）
* 百度链接提交（优化网站的搜索）

# 预览效果

[网站演示](https://www.extlight.com/)

## 前端预览图
![](https://images.extlight.com/portal-01.jpg)
![](https://images.extlight.com/portal-02.jpg)
![](https://images.extlight.com/portal-03.jpg)
![](https://images.extlight.com/portal-04.jpg)

## 后台管理预览图
![](https://images.extlight.com/ml-blog-01.jpg)

![](https://images.extlight.com/ml-blog-02.jpg)

![](https://images.extlight.com/ml-blog-03.jpg)

![](https://images.extlight.com/ml-blog-04.jpg)

![](https://images.extlight.com/ml-blog-05.jpg)

![](https://images.extlight.com/ml-blog-06.jpg)

# 安装

下载源码，执行 resources 目录下的 ml-blog.sql 文件，然后修改 application.properties 中连接数据库的用户名、密码、 lucene 索引生成路径和邮件配置，运行项目即可。

前端访问地址: <http://localhost:8080>

后台访问地址：<http://localhost:8080/admin> 用户名：admin 密码：123456

# 更新日志

2018-06-08 发布

2018-06-12 添加文章分享功能

2018-06-14 添加文章自定义背景图片入口

2018-06-15 添加百度链接提交（主动推送和自动推送）

2018-06-23 添加留言回复以及回复发送邮件通知功能（t_guestbook 表新增 type 和 guestbook_id 字段）

2018-06-28 添加数据库 SQL 文件导出功能
