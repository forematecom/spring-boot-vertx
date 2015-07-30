# spring-boot-vertx

Testing out spring boot and vertx.

The simple idea is to have a Spring controller delegate to the Vertx event bus.
Spring would create a thread per request and put it in the background while the vertx verticles 
handle all the processing. When the verticles are done, the result is sent to the spring deferred 
result and the deferred result returns.

Benefits - Still get the benefits of the massive spring ecosystem and the processing scalabilty of vertx

TODO:

- Figure out how to have a remote event bus
- Understand the how threads are created with Spring MVC and different containers like Tomcat/Undertow
