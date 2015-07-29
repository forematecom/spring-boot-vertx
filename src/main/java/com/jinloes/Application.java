package com.jinloes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by jinloes on 7/29/15.
 */
@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Vertx vertx(){
        return Vertx.vertx();
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = vertx().eventBus();
        eventBus.consumer("get_greeting", message -> {
            System.out.println("I have received a message: " + message.body());
            message.reply("baz");
        });
        return eventBus;
    }
}
