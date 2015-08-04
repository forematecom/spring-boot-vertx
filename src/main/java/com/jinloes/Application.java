package com.jinloes;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

/**
 * Created by jinloes on 7/29/15.
 */
@SpringBootApplication
@EnableAsync
public class Application {
    @Autowired
    private HelloVerticle helloVerticle;

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Vertx vertx() {
        final Vertx[] vertx = {null};
        Vertx.clusteredVertx(new VertxOptions().setHAEnabled(true),
                event -> vertx[0] = event.result());
        while (vertx[0] == null) {
            System.out.println("Vertx hasn't started yet");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        vertx[0].deployVerticle(helloVerticle);
        return vertx[0];
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = vertx().eventBus();
        return eventBus;
    }
}
