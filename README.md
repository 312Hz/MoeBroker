# MoeBroker

> Java 消息中间件，更方便的消息通讯

## 💡项目功能

- 多 client、server 对象运行
- 服务提供者、服务消费者通讯模型

## 🌳项目结构

```bash
.
├─broker-api				# API 接口
├─broker-client				# 客户端模块
├─broker-server				# 服务端模块
├─broker-client-bootstrap	# 客户端启动模块(分离 broker-client 以方便开发者自行实现其他功能)
└─broker-server-bootstrap	# 服务端启动模块(分离 broker-server 以方便开发者自行实现其他功能)
```

## ⚒️技术栈

- SqlFactory(hanhan2001/SqlFactory) -> 数据库操作工具
- Logger(hanhan2001/Logger) -> 日志工具
- javassist -> 字节码工具
- netty -> 通讯框架