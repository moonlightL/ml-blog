-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.40-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.2.0.4949
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 ml-blog 的数据库结构
CREATE DATABASE IF NOT EXISTS `ml-blog` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ml-blog`;


-- 导出  表 ml-blog.t_about_me 结构
CREATE TABLE IF NOT EXISTS `t_about_me` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '内容',
  `status` tinyint(4) NOT NULL COMMENT '是否显示 1：是 0：否',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_about_me 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_about_me` DISABLE KEYS */;
INSERT INTO `t_about_me` (`id`, `content`, `status`, `create_time`, `update_time`) VALUES
	(1, '## 介绍\n从事 Java 开发，同时也喜欢前端技术。\n\n\n\n', 1, '2018-05-25 15:49:31', '2018-06-05 19:52:17');
/*!40000 ALTER TABLE `t_about_me` ENABLE KEYS */;


-- 导出  表 ml-blog.t_category 结构
CREATE TABLE IF NOT EXISTS `t_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `descr` varchar(255) DEFAULT NULL COMMENT '描述',
  `color` varchar(50) DEFAULT NULL COMMENT '背景颜色',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片url',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_category 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `t_category` DISABLE KEYS */;
INSERT INTO `t_category` (`id`, `name`, `sort`, `descr`, `color`, `img_url`, `create_time`, `update_time`) VALUES
	(1, '默认', 1, '默认', 'text-primary', '/portal/images/category_default.jpg', '2018-06-05 19:46:15', '2018-06-05 19:46:19');
/*!40000 ALTER TABLE `t_category` ENABLE KEYS */;


-- 导出  表 ml-blog.t_guestbook 结构
CREATE TABLE IF NOT EXISTS `t_guestbook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  `home_url` varchar(255) DEFAULT NULL COMMENT '主页 URL',
  `img_url` varchar(255) DEFAULT NULL COMMENT '头像 URL',
  `content` varchar(255) NOT NULL COMMENT '留言内容',
  `ip` varchar(50) NOT NULL COMMENT 'IP 地址',
  `ip_addr` varchar(50) NOT NULL COMMENT 'IP 归属地',
  `status` tinyint(4) NOT NULL COMMENT '读取状态 0:未读 1:已读',
  `del_status` tinyint(4) NOT NULL COMMENT '删除状态 0：未删除 1：删除 ',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_guestbook 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_guestbook` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_guestbook` ENABLE KEYS */;


-- 导出  表 ml-blog.t_log 结构
CREATE TABLE IF NOT EXISTS `t_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `method` varchar(255) NOT NULL COMMENT '调用方法',
  `descr` varchar(50) NOT NULL COMMENT '描述',
  `operator` varchar(50) NOT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_log 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;


-- 导出  表 ml-blog.t_param 结构
CREATE TABLE IF NOT EXISTS `t_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param_name` varchar(50) NOT NULL COMMENT '参数名',
  `param_value` varchar(50) DEFAULT NULL COMMENT '参数值',
  `descr` varchar(50) NOT NULL COMMENT '描述',
  `sort` varchar(50) NOT NULL COMMENT '排序',
  `type` tinyint(4) NOT NULL COMMENT '类型：1:全局 2:个人',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `param_name` (`param_name`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='系统参数';

-- 正在导出表  ml-blog.t_param 的数据：~27 rows (大约)
/*!40000 ALTER TABLE `t_param` DISABLE KEYS */;
INSERT INTO `t_param` (`id`, `param_name`, `param_value`, `descr`, `sort`, `type`, `create_time`, `update_time`) VALUES
	(1, 'blogName', '月光中的污点', '网站名称', '1', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(2, 'version', '1.0', '当前版本', '2', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(3, 'author', 'MoonlightL', '开发作者', '3', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(4, 'homePage', 'https://www.extlight.com', '网站首页', '4', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(5, 'server', 'CentOS', '服务器环境', '5', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(6, 'dataBase', 'MySQL5.6', '数据库版本', '6', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(7, 'maxUpload', '10', '最大上传限制(M)', '7', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(8, 'userRights', 'admin', '用户权限', '8', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(9, 'keywords', 'MoonlightL,月光中的污点', '默认关键字', '9', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(10, 'powerby', 'Copyright©2018. Design by MoonlightL', '版权信息', '10', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(11, 'description', '个人博客', '网站描述', '11', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(12, 'record', '浙ICP备17044933号', '网站备案号', '12', 1, '2018-05-21 16:26:00', '2018-05-21 16:26:00'),
	(13, 'qq', '', 'QQ 号码', '0', 2, '2018-05-24 16:40:05', '2018-05-24 16:40:06'),
	(14, 'wx', '', '微信账号', '0', 2, '2018-05-24 16:40:48', '2018-05-24 16:40:49'),
	(15, 'email', '', '邮箱地址', '0', 2, '2018-05-24 16:41:15', '2018-05-24 16:41:16'),
	(16, 'github', '', 'GitHub 主页', '0', 2, '2018-05-24 16:41:56', '2018-05-24 16:41:57'),
	(17, 'wb', '', '微博', '0', 2, '2018-05-24 16:42:19', '2018-05-24 16:42:21'),
	(18, 'qn_domain', '', '七牛云域名', '0', 2, '2018-05-24 16:43:10', '2018-05-24 16:43:14'),
	(19, 'qn_accessKey', '', '七牛云公钥', '0', 2, '2018-05-24 16:43:52', '2018-05-24 16:43:54'),
	(20, 'qn_secretKey', '', '七牛云私钥', '0', 2, '2018-05-24 16:44:17', '2018-05-24 16:44:18'),
	(21, 'qn_bucket', '', '七牛云桶', '0', 2, '2018-05-24 16:44:44', '2018-05-24 16:44:46'),
	(22, 'changyan_app_id', '', '畅言应用 ID', '0', 2, '2018-05-28 13:28:48', '2018-05-28 13:28:50'),
	(23, 'changyan_app_key', '', '畅言应用 Key', '0', 2, '2018-05-28 13:29:14', '2018-05-28 13:29:15'),
	(24, 'geetest_id', '', '极验 ID', '0', 2, '2018-06-04 14:51:02', '2018-06-04 14:51:03'),
	(25, 'geetest_key', '', '极验 KEY', '0', 2, '2018-06-04 14:51:30', '2018-06-04 14:51:31'),
	(26, 'leancloud_app_id', '', 'leancloud 应用 ID', '0', 2, '2018-06-05 16:20:35', '2018-06-05 16:20:36'),
	(27, 'leancloud_app_key', '', 'leancloud 应用 Key', '0', 2, '2018-06-05 16:20:35', '2018-06-05 16:20:36');
/*!40000 ALTER TABLE `t_param` ENABLE KEYS */;


-- 导出  表 ml-blog.t_post 结构
CREATE TABLE IF NOT EXISTS `t_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '标题',
  `keyword` varchar(50) DEFAULT NULL COMMENT '关键字',
  `sub_content` varchar(200) DEFAULT NULL COMMENT '摘要',
  `content` mediumtext NOT NULL COMMENT 'markdown 正文',
  `status` varchar(50) NOT NULL COMMENT '显示状态 1：是 0：否',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `tags` varchar(50) DEFAULT NULL COMMENT '标签',
  `img_url` varchar(50) DEFAULT NULL COMMENT '图片',
  `year` char(50) NOT NULL COMMENT '年',
  `month` char(50) NOT NULL COMMENT '月',
  `day` char(50) NOT NULL COMMENT '日',
  `post_url` varchar(255) NOT NULL COMMENT '文章url',
  `publish_date` date NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `post_url` (`post_url`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_post 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `t_post` DISABLE KEYS */;
INSERT INTO `t_post` (`id`, `title`, `keyword`, `sub_content`, `content`, `status`, `category_id`, `category_name`, `tags`, `img_url`, `year`, `month`, `day`, `post_url`, `publish_date`, `create_time`, `update_time`) VALUES
	(1, 'Hello World', 'hello world', NULL, '## 第一个 Demo\n\n```java\npublic class Test {\n	public static void main(String[] args) {\n		System.out.println("Hello World");\n	}\n}\n```\n\n## 第二个 Demo\n```javascript\nconsole.log("Hello World")\n```', '1', 1, '默认', '', '5.jpg', '2018', '06', '05', '2018/06/05/Hello-World/', '2018-06-05', '2018-06-05 19:55:03', '2018-06-05 19:55:03');
/*!40000 ALTER TABLE `t_post` ENABLE KEYS */;


-- 导出  表 ml-blog.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` tinyint(4) NOT NULL COMMENT '状态 1：正常 0：禁用',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  ml-blog.t_user 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `username`, `nickname`, `password`, `status`, `create_time`, `update_time`) VALUES
	(1, 'admin', '超级管理员', 'e10adc3949ba59abbe56e057f20f883e', 1, '2018-05-24 10:00:09', '2018-05-29 14:17:44');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
