package com.jinloes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by jinloes on 7/29/15.
 */
@SpringBootApplication
public class Application {
    @Autowired
    private HelloVerticle helloVerticle;

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Vertx vertx() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(helloVerticle);
        return vertx;
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = vertx().eventBus();
        return eventBus;
    }
}
