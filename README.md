# javaWebSSH
java版本webssh

[![License](https://img.shields.io/badge/license-GPL%20V3-blue.svg?longCache=true)](https://www.gnu.org/licenses/gpl-3.0.en.html)   
简易在线ssh和sftp工具, 可在线敲命令和上传下载文件. 端口隧道、http代理tcp（待完成）

## 背景：
由于所处特殊行业特殊原因，各地机房防火墙一般ssh协议（跨地域访问）都是封禁状态。
同时很多地方机房都不提供诸如堡垒机等形式的运维工具、方法，
造成跨地域的运维工作很难开展。故意尝试使用webssh方式。
由于业务、部署环境的特殊性，java目前市面上没有很合适的java版本的webssh可供使用，

故参照 [go webssh](https://github.com/Jrohy/webssh) 实现了一版java版本的webssh。

## 功能:
实现的java版本的webssh

## 原理
```
+---------+     http     +--------+    ssh    +-----------+
| browser | <==========> | webssh | <=======> | ssh server|
+---------+   websocket  +--------+    ssh    +-----------+
```

### go webssh已有功能：
1. 密码方式登录ssh.
2. 私钥方式登录ssh. 
3. 文件上传下载. 

### 添加如下功能：
1. 独立的本地认证
2. 端口转发（TODO）
3. 可独立部署 (TODO)
4. 打包到已有应用中部署适配。（TODO）


## 部署方式

### 独立部署
1. 安装node等软件
2. cmd 进入vue2-web\web, 执行`npm run build`，打包前端资源
3. 修改vue2-server\src\resources\application-prod.yml中webssh的参数，参数详见下文。
4. cmd 进入项目根目录，mvn package
5. copy vue2-server\target\vue2-sshserver-1.26-SNAPSHOT.jar到服务器，使用 `java -jar vue2-sshserver-1.26-SNAPSHOT.jar` 启动程序.
6. 浏览器访问 http://127.0.0.1:8357/webssh/index 即可反问

### docker独立部署
TODO

### 包含到已有项目中
TODO

**其他语言版本: [English](README_en.md), [中文](README.md).**

