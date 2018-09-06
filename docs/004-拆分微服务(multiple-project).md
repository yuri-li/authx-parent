---
typora-root-url: images
---



# 1 简介

> 在真实的环境中，应该拆分成3个独立的项目。但，学习嘛，一点点做。

拆分成三个子模块：

- common，提取公共的配置

- server，负责认证。且，只提供内部接口
- account开放login的接口

项目构建的顺序：

- authx-parent
- common
- server
- account

# 2 authx-parent

建议：只在parent中编写build.gradle.kts

> 也可以编写代码。但是，按照习惯（maven的习惯，虽然换了gradle，但，没有必要，不用改习惯），删除目录`src`

## 2.1 plugins

![1536206643955](.\1536206643955.png)

> 图片有点瑕疵，其中，`org.jetbrains.kotlin.jvm`需要设置`apply=true`。因为在当前项目中，需要配置公共的dependency，而`compile`命令，依赖`kotlin`的插件。总之，现在就要用

## 2.2 allporjects

![1536226194229](.\1536226194229.png)

## 2.3 subprojects

```
subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    if (!"common".equals(project.name)) {
        apply {
            plugin("org.springframework.boot")
            plugin("org.jetbrains.kotlin.plugin.spring")
            plugin("io.spring.dependency-management")
            plugin("org.jetbrains.kotlin.plugin.allopen")
        }

        dependencies {
            compile("XXX")
           
            testCompile("XXXX")
        }
    }
}
```

## 2.4 dependencies

集中管理subproject的依赖

```
dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
```

# 3 common

`File -> New -> Module`

只有一些公共的model/util，所以，依赖项很少。

> intellij idea对gradle的支持并不完美，所以，按照官网`kotlin-dsl`的示例，修改`settings.gradle.kts`后，每个新建的module，都需要手工添加
>
> ```
> include("common")
> ```

# 4 server & account

除了跟auth相关的代码外，其他特性与common相同。所以，略。

# 5 启动项目

推荐使用view`run dashboard`管理spring boot的项目

## 5.1 配置idea64.exe.vmoptions

在最后一行添加：

```
-Dide.run.dashboard=true
```

保存文件，且，重启intellij idea

## 5.2 run dashboard

首次加载，会给出提示，启用即可看到新的视图`run dashboard`

![1536229021726](/1536229021726.png) 

点击左侧的启动按钮，即可

# 6 测试

## 6.1 `run dashboard`

可以同时启动多个module的服务

![1536229183374](/1536229183374.png)

## 6.2 login

![1536229143543](/1536229143543.png)

## 6.3 使用token访问接口

![1536229221400](/1536229221400.png)





