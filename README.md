# BLOG

问题记录：

10-10：

控制台日志不输出：log4j2.xml的配置问题

10-11：

thymeleaf无法获取工具类：在拦截器中配置工具类的上下文
       
MapCache报空指针，单例的返回没有初始化缓存池
       
10-12

在多处代码中发现异常捕获与事务回滚的冲突，还没想到好的解决方案

目前的想法：前端在提交表单后，如果收不到返回的响应数据，再发起一次异常请求，而后端取消对异常的捕获，并将异常信息
做缓存

10-16 

对于之前的事务异常有新的想法，用统一的异常处理器ExceptionHandler处理

10-24

关于ssl配置的bug：

在application.properties中的端口配置为server.port=8443，而在配置类中：

       @Bean
        public Connector httpConnector(){
            Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            connector.setSecure(false);
            connector.setRedirectPort(8443);
            return connector;
        }

mvn install -DskipTests

mvn install -Dmaven.test.skip=true

ps -ef|grep BLOG

kill -9 pid

nohup java -jar BLOG.jar

ssl的部署仍然存在问题