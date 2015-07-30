package com.jinloes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.springframework.stereotype.Component;

/**
 * Created by jinloes on 7/29/15.
 */
@Component
public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().consumer("helloService", this::onMessage);
    }

    private void onMessage(Message<String> message) {
        message.reply("baz");
    }
}
