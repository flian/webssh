**其他语言版本: [English](README_en.md), [中文](README.md).**

# java版本webssh

java版本 webssh

[![License](https://img.shields.io/badge/license-GPL%20V3-blue.svg?longCache=true)](https://www.gnu.org/licenses/gpl-3.0.en.html)
做最好用的java在线ssh和sftp工具, 可在线敲命令和上传下载文件. 端口隧道、http代理tcp。
RDP远程、管理windows服务器
VNC协议远程、管理远程服务器

## 运行截图

![avatar](asset/1.png)
![avatar](asset/2.png)
![avatar](asset/3.jpg)
![avatar](asset/4.jpg)

## 背景

项目上的需要，需要非常轻的"堡垒机",但市面上的java webssh都有各种问题:
主要问题如下:
1. 安全级别不够，都是http/ws. 
2. jsch使用各种小问题，安全隐患等。
3. 部署不够灵活，只要独立部署。项目上有时候需要”内嵌"到已有项目。
4. 其他一些定制化需要，不够灵活，比如项目上大量使用navicat运维，需要轻便的数据库运维等等。
5. 其他一些定制化需求。

如果你也有如上问题，那么本项目可能非常适合你。

1.26: 完成基础的web ssh功能，包括ssh，sftp文件上传下载。权限加强等。
1.27: 优化权限认证等，完善navicat（mysql,sqlite,pgsql） http tunnel(mongodb TBD后续会尝试完善). RDP协议远程、管理windows服务器。
1.27.1: http代理、socket代理. VNC协议管理远程服务器。

## 功能

实现的java版本的webssh。
业务代码依赖webssh-core和vue2-web，配置参数即可随业务一起开启webssh功能。

## 原理

```
+---------+     http     +--------+    ssh    +-----------+
| browser | <==========> | webssh | <=======> | ssh server|
+---------+   websocket  +--------+    ssh    +-----------+
```

### 更新日志

#### v.127. 更新中
1. http/socket 代理。 GA(基本可用)
   服务端配置、打开代理端口及服务，客户端使用proxifier代理本地流量

2. vnc服务器管理。 GA(基本可用)
   服务端安装VNC server（建议tightVNC）,设置好防火墙规则和账号，在webssh中直接远程服务器。


#### v1.27 更新中

1. rdp协议支持（可以通过linux webssh rdp协议远程连接windows服务器）GA（基本可用)
   使用场景，客户端（windows）透过webssh服务器（webssh主机或者其他linux服务器）访问远端windows服务器
   前置条件及测试步骤：
   a.客户端需要安装xming并启动
   b.中转服务器需要有X11支持。（centos需要安装xorg-x11-xauth xorg-x11-fonts-* xorg-x11-utils并且/etc/ssh/sshd_config中`X11Forwarding yes`
   c. 可以常规通过webssh登录中转linux服务器中，输入xclock，如果windows客户端弹出时钟界面表示配置成功
   手动触发连接服务器模式

连接中转服务器时直接触发模式：
TBD

2. navicat ntunnel for mysql,pgsql,sqlite
   mysql 隧道: /php/navicat/ntunnel/mysql?token={}
   pgsql 隧道: /php/navicat/ntunnel/pgsql?token={}
   sqlite 隧道: /php/navicat/ntunnel/sqlite?token={}

#### v1.26 更新 2024-02-02

完成基本功能：

1. 密码方式登录ssh.
2. 私钥方式登录ssh.
3. 文件上传下载.
4. 独立部署.
5. 独立的本地认证
6. 打包到已有应用中部署适配。

### 主要功能

1. 密码方式登录ssh.
2. 私钥方式登录ssh.
3. 文件上传下载.
4. 独立部署.
5. 独立的本地认证
6. 端口转发（TODO）
7. 打包到已有应用中部署适配。
8. navicat http代理。 (TODO)
9. windows rdp支持。 TODO

## 部署方式

### docker独立部署

v1.26示例(windows):

```
docker run  -d -p 5132:5132 -p 5443:5443  --restart always  --name javawebssh -e JAVA_OPTS=“-Xmx1024M -Xms1024M” -e SPRING_BOOT_OPTS=“--spring.profiles.active=docker --webssh.allowedUsers=root:changeit@123![RANDOM]:%,test:test@123!:127.0.0.1” foylian/webssh:1.26
```

v1.26示例(linux):

```
docker run  -d -p 5132:5132 -p 5443:5443  --restart always  --name javawebssh -e JAVA_OPTS='-Xmx1024M -Xms1024M' -e SPRING_BOOT_OPTS='--spring.profiles.active=docker --webssh.allowedUsers=root:changeit@123![RANDOM]:%,test:test@123!:127.0.0.1' foylian/webssh:1.26
```

可能有网络问题，不行试试 `--net=host` 参数？

关键含义说明：

浏览器访问 [http 5132](http://127.0.0.1:5132/webssh/index) 或者 [https 5443](https://127.0.0.1:5443/webssh/index) 即可访问.
强烈建议自行修改其中"--webssh.allowedUsers=root:changeit@123![RANDOM]:%,test:test@123!:127.0.0.1"配置的账户密码,

其中"[RANDOM]"表示启动时随机产生字符串占位符,这里使用的默认账户配置，
test用户为简单密码，生产环境一定要换掉或者去掉.(因为可以在本地启用nginx等反向代理，强行制造访问端ip为127.0.0.1跳过ip限制)

上面的"--webssh.allowedUsers="参数配置含义含义如下:

账户1：

账户账户名：root

账户密码：changeit@123! +（系统启动时会随机参数一串字符串），需要在控制台找最终的密码

允许登录ip：任意ip

账户2：

账户账户名：test

账户密码：test@123!

允许登录ip：只允许客户端ip为127.0.0.1的电脑登录

docker profile启动会在第一次启动时自动生成ssl证书文件，所以建议使用。
java内存默认设置1G，演示用，自由调整。
其他更多参数设置，见[关键参数说明](#关键参数说明)

### 独立部署

[release v1.26](https://github.com/flian/webssh/releases/tag/V1.26)
可以到上面对应的发布版本页面下载已经打包好的spring boot jar包。

或者自己编译包，参考如下步骤：

1. 安装node等软件
2. cmd 进入vue2-web\web, 执行`npm run build`，打包前端资源
   (可选，dev分支会不定期提交编译好的前端代码。要保证最新，可以自己编译一下前端页面。)
3. 修改vue2-sshserver\src\resources\application-prod.yml中webssh的参数，见[关键参数说明](#关键参数说明)
   强烈建议修改`--webssh.allowedUsers`设置允许登录webssh的账号密码信息。
4. cmd 进入项目根目录，mvn package
5. copy vue2-sshserver\target\vue2-sshserver-1.26.jar到服务器， 使用 ``` java -jar vue2-sshserver-1.26.jar --webssh.allowedUsers=root:changeit@123!:%``` 启动程序.
   其中changeit@123!改为自己的密码。如果不设置webssh.allowedUsers,默认的root会产生一个随机密码，请注意观察控制台日志。
6. 浏览器访问 [http 5132](http://127.0.0.1:5132/webssh/index) 或者 [https 5443](https://127.0.0.1:5443/webssh/index) 即可访问.
7. 默认的standalone会开启https，5132会转跳到`https://127.0.0.1:5443/webssh/index`
8. 可通过设置启动参数`--spring.profiles.active=http`使用http only模式。（不建议使用，webssh会直接操作系统，太敏感了）
9. 建议发布生成环境时产生并使用自己的ssl证书。
   自签名证书生成及配置示例：
   例如，cd d:\执行命令：```keytool -genkey -alias springboottomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore websshDefault.p12 -validity 3650 -dname "CN=webssh, OU=NoOU, O=NoO, L=Chendu, ST=Sichuan, C=cn" -storepass "webssh@Admin123!"  -keypass "webssh@Admin123!"```

其中alias,keystore,storepass,keypass按需要配置

完成后在d根目录很产生keystore配置文件名的.p12文件，copy到合适的位置

启动命令参考application-prod.yml，配置ssl文件名、路径、密码等信息,后续版本会考虑动态启动时动态生成ssl文件并配置

10. 其他更多设置，见[关键参数说明](#关键参数说明)

### 包含到已有项目中

包含到已有项目后，主要问题就是认证的问题，下面假设已有项目都包含了webssh的全部前端、后端代码。
由于目前还在开发、测试阶段，执行下面操作时，请自安装webssh到本地（命令行执行`mvn install`）或者自己的私库。

#### [必选]项目依赖

webssh开发环境实在spring boot 2.5.14版本下，讲道理大于这个版本，小于3的版本都是支持的。
webssh必须依赖的组件包括springboot配套的websocket,validation两个模块，下面是项目依赖
的示例,请根据项目情况增减:

```
        <dependency>
            <groupId>org.lotus.carp.webssh</groupId>
            <artifactId>webssh-core</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.lotus.carp.webssh</groupId>
            <artifactId>vue2-web</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```

同时，无论如何都需要把以下webssh api加入项目白名单：

/webssh/index

/webssh/check

/webssh/shouldVerifyToken

/webssh/projectHeader/params

/webssh/login

/webssh/logout

#### [可选1]只启用webssh认证

webssh端配置:
默认配置已经开启了webssh认证，只需要项目里面配置'webssh.allowedUsers:'参数即可。
由于只依赖webssh自己的认证，这里密码强度请注意设置足够复杂，并且请注意不要泄漏密码。

其他更多参数设置，见[关键参数说明](#关键参数说明)

项目端配置：
由于只启动webssh认证，项目端需要把`/webssh/**`加入项目自己的白名单中即可。

#### [可选2]使用项目认证方式认证webssh接口

实现`WebSshProjectTokensApi.composeProjectHeaderTokens` 方法,把对应用户后续请求需要带在header里面的参数设置上。
例如：
SampleProjectHeaderController示里里面返回了一个"new ProjectHeaderParamVo("AUTH_COOKIE_TEST", RandomUtils.generatePassword(8))"
webssh后续请求头里面就会有一个AUTH_COOKIE_TEST参数，参数值为这里设置的一个随机字符串。
实现时，这里获取当前用户的身份信息，返回一个项目使用的token，这样webssh请求都会经过项目的正常认证流程。

其他更多参数设置，见[关键参数说明](#关键参数说明)

#### 配置webssh

最后把`/webssh/index`加入已有项目的正常菜单、权限管理即可。更详细的webssh按钮、功能权限后续规划中，敬请期待。

其他更多参数设置，见[关键参数说明](#关键参数说明)

### 关键参数说明

webssh.allowedUsers: webssh独立认证中的用户配置信息。格式"用户名:密码：允许登录的ip列表"
,默认值“root:changeit@123![RANDOM]:%,test:test@123!:127.0.0.1”

表示含义，

root:changeit@123![RANDOM]:%

允许登录用户名:root

密码：changeit@123!+系统随机产生的随机字符，

允许登录者的ip：允许任意ip登录。

test:test@123!:127.0.0.1

允许登录用户名:test

密码：test@123!

允许登录者的ip：127.0.0.1

强烈建议使用的时候自己配置这个参数，并且保密。
如果不想使用配置方式开启webssh认证，可设置webssh.forceCheckUserConfig2Prod=false,
并且自行实现WebSshLoginService接口。

webssh.shouldVerifyToken: 是否开启webssh页面及api的独立认证。默认值:true.

webssh默认包含一个token认证，为true时会有个独立登录页面，
认证页面会验证webssh.allowedUsers配置的用户，成功后会返回一个
token给前端，后续webssh的接口调用会带上这个token。

webssh.tokenExpiration: webssh独立认证token的过期时间。默认值：6,单位小时。

webssh.savePass: 前端"历史记录"中是否记住ssh的密码,
设置为true后会在前端的localstorage中存储ssh密码，不建议开启，默认值:false.

webssh.debugJsch2SystemError: 是否打开jsch的debug信息，
当需要调试源代码时可以开启。默认值：false

webssh.forceCheckUserConfig2Prod: 是否开启严格验证webssh.allowedUsers
配置的用户信息。默认值true. 开启后会验证账号的强度。

webssh.enableRandomPwd: 启动时，是否使用随机字符串替换掉webssh.allowedUsers配置中
[RANDOM]字段,并在控制台打印产生的密码信息。默认值true。

webssh.defaultConnectTimeOut: jsch连接远程ssh server是的默认超时时间.
默认值30*1000，表示30s

webssh.webSshTermPtyType: jsch连接server的ttype类型，默认值：xterm。可选:vt100等。
具体请查看SSH2的RFC。

webssh.sshPrivateKeyOnly: jsch连接远程server的时候，是否启用密码登录方式认证。
默认值：false. 表示密码和私钥都允许登录。
在特定环境时可以设置为true，表示只允许私钥方式登录远程服务器。

webssh.foreHttps: 是否强制开启webssh https。默认值：false
项目依赖模式，这里是关闭的了，配置随着主项目配置
standalone默认是true，因为webssh里面跑的信息都太敏感了，https能稍微加强一点安全性。
(强烈建议standalone模式的同学自己生成自己的ssl证书，不要用默认的配置)

webssh.underContainer: 应用使用的容器名称。默认值："",
配合webssh.foreHttps=true的时候才有意义
项目依赖模式，这里默认值就是”“，即没有启用默认http转跳https的配置
standalone模式默认值这里是“tomcat",会启用TomcatHttpsConfig里面的配置，
启用http强制转跳https，具体配置参数vue2-sshserver模块

webssh.dateFormat: webssh页面上涉及到的日期格式。默认值："yyyy-MM-dd HH:mm:ss"

webssh.maxSshShellTermCorePoolSize: webssh中能打开的最大控制台数量。默认值：1000

webssh.randomPwdWord： 默认值[RANDOM],启动时，密码中需要产生随机字符产生替换的字符串。

### webssh api说明

```
/webssh/index  webssh主页
/webssh/check  检查ssh账号密码有效性
/webssh/shouldVerifyToken  拉取是否启用webssh独立认证
/webssh/projectHeader/params  拉取webssh api请求时需要的额外token
/webssh/login webssh独立认证登录接口
/webssh/logout webssh独立认证注销接口
/webssh/file/list 拉取对于终端的文件目录信息
/webssh/file/download 下载远程server指定的文件
/webssh/file/upload 上传指定文件到指定目录

websocket url(ws/wss):
 /webssh/term 建立xterm终端连接
 /webssh/file/progress 获取指定文件上传进度


```

### mongodb扩展安装

[mongoDB扩展驱动安装](https://www.runoob.com/mongodb/mongodb-install-php-driver.html)

or [refer](https://www.php.net/manual/en/mongodb.installation.php)

mac os can install with:

```
brew install shivammathur/extensions/mongodb@8.4
```

修改webssh-navicat-tunnel模块中php.ini配置

### 其他开发相关

更多模块详情，请查看对应模块README描述

发布到私服： mvn deploy -Dmaven.test.skip=true

版本发布： see [maven-release-plugin](https://maven.apache.org/maven-release/maven-release-plugin/) for more detail

预备：mvn release:prepare
发布：mvn release:perform
