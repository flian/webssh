# javaWebSSH
java版本webssh

## 背景：
由于所处特殊行业特殊原因，各地机房防火墙一般ssh协议（跨地域访问）都是封禁状态。
同时很多地方机房都不提供诸如堡垒机等
形式的运维工具、方法，造成跨地域的运维工作很难开展。故意尝试使用webssh方式。
由于业务、部署环境的特殊性，java目前市面上没有很合适的java版本的webssh可供使用，
故参照 [go webssh](https://github.com/Jrohy/webssh) 实现了一版java版本的webssh。

java version origin from: 

## 参考:

[go webssh](https://github.com/Jrohy/webssh) version: v1.26

## 功能:
实现的java版本的webssh

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
TODO

### docker独立部署
TODO

### 包含到已有项目中
TODO

**其他语言版本: [English](README_en.md), [中文](README.md).**

