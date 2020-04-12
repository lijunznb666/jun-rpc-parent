# jun-rpc

一个基于Java 的RPC 框架，包含注册中心：提供服务注册和发现

1. 网络通信采用的是 Netty4
2. 注册中心利用的是Zookeeper 
3. 动态代理采用 byte-buddy
4. 序列化采用Protobuff (可拓展 例如 fastjson、jackjson 以及java 序列化等)
5. 核心代码脱离spring、springboot 可以通过api 调用
6. 集成Spring 提供XML Java配置, 继承SpringBoot 引用即可使用
7. 提供SPI机制，实现微内核加插件架构，实现可扩展



### 使用

三种情况 支持以下使用方式:    
1. 原生API形式，不依赖Spring,非Spring项目也可以使用
2. Spring配置方式，和Spring很好的集成
3. Spring Boot配置方式，提供了一个spring boot starter，以自动配置，快速启动

# API使用
本框架核心代码不依赖Spring，可脱离Spring使用。        
1. 启动ZK注册中心

2. 编写一个接口IHelloService

    ```java
    public interface IHelloService {
        String hello(String name);
    }
    ```

3. 编写一个IHelloService的实现

    ```java
    public class HelloService implements IHelloService {
        @Override
        public String hello(String name){
            return "Hello, " + name;
        }
    }
    ```

4. 启动Server

    ```java
    IRegistry registry = new EtcdRegistry("http://127.0.0.1:2181");
    RpcServer server = new RpcServer(registry)
            .port(2017)
            .exposeService(IHelloService.class,new HelloService());
    server.run();
    ```

5. 启动client

    ```java
    RpcClient client = new RpcClient(registry);
    IHelloService helloService = client.create(IHelloService.class);
    String s = helloService.hello("world");
    System.out.println(s);   // Hello, world
    ```

6. 注意事项

    先启动server 再启动 provider 否则会出现找不到提供者错误     
# Spring配置

参考Dubbo  提供@Service @Reference 注解

==服务提供者，使用自定义注解@Service来暴露服务，通过interfaceClass来指定服务的接口。        
该@Service注解是rpc提供的，并非Spring的注解==

```java
@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "hello" + name;
    }
}
```
==服务使用者，通过@Reference来引用远程服务，就像使用本地的SpringBean一样。背后的SpringBean封装和Rpc调用对开发者透明。使用体验和Dubbo是一样的。==

```java
public class TestHello {
    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public void hello(String name) throws Exception {
        System.out.println(helloService.hello(name));
    }
}

```
配置服务提供者，本例子使用XML配置，使用Java Code配置也可以。
```xml
<bean id="registry" class="com.lijun.rpc.registry.EtcdRegistry">
        <constructor-arg name="registryAddress" value="http://127.0.0.1:2181"></constructor-arg>
    </bean>
    <bean id="server" class="com.lijun.rpc.server.RpcServer">
        <constructor-arg name="registry" ref="registry"></constructor-arg>
    </bean>

    <bean id="serviceAnnotationBeanPostProcessor" class="com.lijun.rpc.spring.ServiceAnnotationBeanPostProcessor"></bean>

    <bean id="helloService" class=".....HelloService"></bean>
```
配置服务消费者，本例子使用XML配置，使用Java Code配置也可以。
```xml
<bean id="registry" class="com.lijun.rpc.registry.ZookerRegistry">
        <constructor-arg name="registryAddress" value="http://127.0.0.1:2379"></constructor-arg>
    </bean>

    <bean id="client" class="com.lijun.rpc.client.RpcClient">
        <constructor-arg name="registry" ref="registry"></constructor-arg>
    </bean>

    <bean id="referenceAnnotationBeanPostProcessor" class="com.lijun.rpc.spring.ReferenceAnnotationBeanPostProcessor"></bean>

    <bean id="Test" class=".....TestHello"></bean>
```

# Spring Boot配置
使用原生的Spring配置还是有些繁琐，可以使用Spring Boot来获得更好的开发体验。        
服务提供者
```java
@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "Hello, " + name ;
    }
}
```
服务消费者
```java
@Component
public class HelloTest {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public String hello(String name) throws Exception {
        return helloService.hello(name);
    }
}
```
在application.properties文件中配置服务提供者
```properties
jun.rpc.registry.address=http://127.0.0.1:2181

jun.rpc.server.enable=true
jun.rpc.server.port=2017
jun.rpc.annotation.package=....
```
在application.properties文件中配置服务消费者
```properties
jun.rpc.registry.address=http://127.0.0.1:2181

jun.rpc.client.enable=true
```
使用SpringBoot时，不许再手动配置相关的spring bean，本Rpc框架提供的spring boot starter会自动配置好这些spring bean。
