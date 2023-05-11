# Spring Cloud Alibaba 整合Nacos单机版
[详细过程记录](https://xiaoyver.top/articles/2023/05/11/1683784904915.html)


## 说明
1. `.flattened-pom.xml`是`flatten-maven-plugin`这个maven插件产生的,用于maven的jar包版本控制
2. 引入`spring-boot-starter-validation`验证框架的依赖,是因为我在第一整合时候引入了hibernate的那个,@Valid和@Validated不生效,这里引入了可以让使用这部分功能的童鞋少走一点弯路
