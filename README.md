> 建议：
>
> - 使用（最新版的）intellij idea查看项目源码
> - 使用（Typora）查看文档

# 1 简介

模拟keycloak的功能。

## 1.1 核心

权限管理的核心，就两块：认证、授权

| 类别 | 描述     | 专用名词       |
| ---- | -------- | -------------- |
| 认证 | 你是谁   | authentication |
| 授权 | 能做什么 | authorization  |

**思考：观察下图，区分“认证”与“授权”**

![1531126909071](./docs/images/1531126909071.png)

## 1.2 OAuth2.0



# 2 版本管理

| 版本号 | 状态 | 功能列表                                                     |
| ------ | ---- | ------------------------------------------------------------ |
| 基本认证 | √    | 实现最基本的登录功能（不使用数据库，直接在代码里写死client/user/role等数据） <br/>通过token，获取用户的Authentication <br/>添加日志/swagger等辅助功能 |
| 摘要认证 | ×    | 实现digest authentication                                    |
| 扩展登录字段 |   √   |   按自己的需要加载用户的数据（不局限于loadUserByUsername）                                                           |
|拆分微服务（multiple-project）| √ | 拆分项目：公共的服务/认证服务端/认证客户端                   |
|simple-RBAC|  | 基于角色的权限控制                                           |
|role-hierarchy|  | 层级角色：ADMIN > AGENT > USER                               |
||  |                                                              |
||  |                                                              |
||  |                                                              |












