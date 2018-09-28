

# 1 简介

BUG：线上环境，auth服务单独一台redis服务器。高峰时，redis几乎占用了全部的网络带宽

解决方案：使用auth服务器的本地缓存，缓存token相关的数据。

- 因为auth服务器是集群部署的，如果改成本地缓存，可以减少大部分对redis的请求
- 但，本地缓存有可能是脏数据。设置一个合理的失效时间，可以减少本地缓存里的脏数据

# 2 配置

```
spring:
  cache:
    type: CAFFEINE
    caffeine:
      spec: maximumSize=2000000,expireAfterWrite=10s
```

- maximumSize：最大条数。

  设，JVM分配的内存10GB，允许本地缓存最多使用80%，每条记录占用空间4KB，则，
  $$
  maximumSize = \dfrac{10 * 0.8 * 1024 * 1024}{4} \approx 200万
  $$

- expireAfterWrite：过期时间。
  高峰时间段t秒，过期时间x秒，每次从redis加载数据耗时0.02秒，访问redis的频率ν，总共需要暂停n次：

$$
\begin{cases}
(x+0.02) * n = t \ \ ⇒ \ \ \ \ x = \dfrac{t}{n} -0.02\cr
ν = \dfrac{0.02 * n}{t} \ \ ⇒ \ \ \ \ \ \ \ \ \ \ \dfrac{t}{n} = \dfrac{0.02}{ν}
\end{cases}
\ \ \ ⇒ \ \ \ \ x = 0.02 * (\dfrac{1}{ν} - 1)
$$

$$
令 \ ν = 1 \% ⇒ x \approx  2
$$

$$
令 \ ν = 0.1 \% ⇒ x \approx  20
$$

	考虑到本地缓存的脏数据对业务的影响，取`expireAfterWrite=10s`，每500次请求，访问一次redis

