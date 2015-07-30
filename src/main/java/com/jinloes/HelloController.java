package com.jinloes;

import io.vertx.core.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinloes on 7/29/15.
 */
@RestController
public class HelloController {
    private final EventBus eventBus;

    @Autowired
    public HelloController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Gets the hello message in a background thread. Returning a default value if it takes
     * longer than 1ms to run.
     *
     * @param waitSecs number of seconds to wait, for testing
     * @return hello message
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<String>> getGreeting(
            @RequestParam(defaultValue = "0") int waitSecs) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(1L,
                new ResponseEntity<>("foo", HttpStatus.OK));
        eventBus.send("helloService", "Get the greeting", asyncResult -> {
            String body = Objects.toString(asyncResult.result().body());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSecs));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deferredResult.setResult(new ResponseEntity<>(body, HttpStatus.OK));
        });
        return deferredResult;
    }
}
