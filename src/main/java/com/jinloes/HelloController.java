package com.jinloes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinloes on 7/29/15.
 */
@RestController
public class HelloController {
    private final EventBus eventBus;
    @Autowired
    private TodoService todoService;
    @Autowired
    private Vertx vertx;

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
    public DeferredResult<ResponseEntity<Map<String, String>>> getGreeting(
            @RequestParam(defaultValue = "0") int waitSecs) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "foo");
        DeferredResult<ResponseEntity<Map<String, String>>> deferredResult = new DeferredResult<>(
                1L, new ResponseEntity<>(response, HttpStatus.OK));
        eventBus.send("helloService", "Get the greeting", asyncResult -> {
            Map<String, String> body = new HashMap<>();
            body.put("message", Objects.toString(asyncResult.result().body()));
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSecs));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deferredResult.setResult(new ResponseEntity<>(body, HttpStatus.OK));
        });
        eventBus.send("getTodo", "abc123", event -> {
            System.out.println(event.result().body());
        });
        return deferredResult;
    }

    @RequestMapping(value = "/todos/{toDoId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<String>> getTodo(@PathVariable("toDoId") String todoId) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        eventBus.send("getTodo", todoId, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> event) {
                deferredResult.setResult(new ResponseEntity<>(event.result().body().encodePrettily(),
                        HttpStatus.OK));
            }
        });
        return deferredResult;
    }

    @RequestMapping(value = "/todos_old/{toDoId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Callable<JsonObject> getTodoOld(@PathVariable("toDoId") String todoId) {
        return () -> todoService.getTodo(todoId);
    }
}
