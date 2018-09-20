---
typora-root-url: images
---

# 1 简介

hasRole的粒度很粗，慎用

# 2 代码

```
    @GetMapping("/findUser/{username}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun findUser(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String){
        log.info("find user by username:${username}")
    }

    @DeleteMapping("/delUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delUser(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String) {
        log.info("delUser ${username}")
    }

    @GetMapping("/findAccount/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun findAccount(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String){
        log.info("find account by username:${username}")
    }
```

# 3 测试

测试的接口与参数都已经设置好了，请将[hasRole.json](./postman/hasRole.json)导入postman

![1537440145338](/1537440145338.png)