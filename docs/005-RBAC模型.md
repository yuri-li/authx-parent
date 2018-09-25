---
typora-root-url: images
---



# 1 为什么要有角色


![rbac](/rbac.png)

角色，很抽象的概念：

| 约束类型   | 释义                                               | 举例                           |
| ---------- | -------------------------------------------------- | ------------------------------ |
| 自由组合   | 一个人可以同时拥有任意个角色                       | 身兼多职                       |
| 继承       | 拥有parent node，自动获得All children roles 的权限 | 中国家长                       |
| 互斥       | 互斥的角色，一个用户只能拥有其中的一个             | 男、女                         |
| 运行时互斥 | 给一个用户配置多个角色，但，运行时，只能激活一个   | QQ、360                        |
| 限定个数   | 一个角色，只能授予有限的用户                       | 主席                           |
| 按顺序演变 |                                                    | 婴儿、儿童、少年...            |
| 运行时计算 |                                                    | 判断操作员是否资源持有者的上级 |

# 2 什么是RBAC模型

RBAC：基于角色的权限模型

目的：判断用户能否操作资源
表现：用户、角色、资源之间的连线

![1536889025557](/1536889025557.png)

- 用户：真实存在的个体（真人）
- 角色
  - 从业务的角度，对用户分组。比如：管理员、代理、会员、游客等；
  - 从功能的角度，也可以对用户分组，比如：readOnly、write等
- 资源 ：后端提供的接口、网页、图片、文件等，都是资源

# 3 原则

单向设置权限：`user --> role --> resource` 

![1537156778800](/1537156778800.png)

# 4 组

用户组，通常是组织结构，比如，XXX公司XXX部门XXX小组
角色组，比如，平台服务商、租户、子账号（租户的管理员）、上级、直属上级、下级、直属下级、客户、游客等
资源组，比如，图片、文档、URL等


# 5 练习

| 类别 | 示例                                                         |
| ---- | ------------------------------------------------------------ |
| 层级 | 论坛系统，超级管理员、普通管理员、版主等，上级拥有下级的所有权限 |
| 角色 | 超市管理系统，收银员、开票员、仓管员等，用户之间的地位是平等的，分别对应不同的应用模块 |
| 资源 | 博客系统，文章的增删改查等操作，都是资源                     |
| 流程 | 公文系统，基层科员起草-->科长审批-->办公室校对-->局长签发。如果公文不在当前用户的环节，即使是局长也无权修改 |

分析：

- 层级和角色，都可以抽象为“角色”
- 在流程中，不能把“公文”看做“资源”，节点才是

# 6 权限验证

判断用户是否能查看项目的报表

思考：

1.  配置的资源应该是项目，还是报表？
2. 查看报表是个接口。这个接口是资源吗？

参考答案：

1. 假设，一个项目有多份报表。配置的粒度，要分情况：

   a) 如果每一份报表都需要管理权限。则，资源是：报表

   b) 如果允许用户查看这个项目所有的报表。则，资源是：项目

2. 接口的属性很多：function name、params、return value、rest path等。而spring 

## 6.1 权限配置

| user | role        | resource |
| ---- | ----------- | -------- |
| U001 | READ_REPORT | P001     |

## 6.2 基于角色的验证

```
	@GetMapping("/report/{reportId}")
    @PreAuthorize("hasRole('READ_REPORT')")
    fun readReport(
      @ApiIgnore @AuthenticationPrincipal user: CurrentUser,
    	@RequestParam("reportId") reportId:String
    ): Report {
        ...
    }
```

|      | 描述                                                         |
| ---- | ------------------------------------------------------------ |
| 缺点 | annotation也是硬编码<br/>只能判断角色，并不能判断P001是否允许U001访问 |
| 优点 | 权限都写死在代码里了，用户不需要再单独配置，简单！网上可以找到很多示例 |



- @ApiIgnore。指示swagger开放的接口，忽略这个参数

- @AuthenticationPrincipal

  - 首先，访问这个接口必须传入user token
  - 其次，spring会将authentication自动注入当前变量

- CurrentUser。自定义的model

  > spring security 封装的authentication兼容client与resourceOwner。使用的时候，很不方便。所以，

## 6.3 基于语义的验证

```
@RequestMapping(value = ["/report/{reportId}"], method = [RequestMethod.GET])
    fun readReport(@ApiIgnore @AuthenticationPrincipal user: CurrentUser,
    	@RequestParam("reportId") reportId:String
    ): Report {
        if(user.resources.contains(reportId)){
            //返回报表的数据
        }else{
            //提示用户，没有权限
        }
    }
```

- `user.resources` 。不能直接给user挂载resource，但是，挂载完成后，可以直接获取
- `contains(reportId)` 。只是演示。实际情况，语义更复杂些
  - 根据reportId，查找这个报表属于哪个项目。根据项目Id，查找负责人
  - 项目负责人与U001的关系，从而，判断，是否有权限

## 6.4 总结

建议：使用基于语义的验证 + spring的AOP 或 interceptor，将权限做成可配置的



