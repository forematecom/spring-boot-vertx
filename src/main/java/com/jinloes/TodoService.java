package com.jinloes;

import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rr2re on 8/4/2015.
 */
@Service
public class TodoService {
    private static final Map<String, JsonObject> TODOS;

    static {
        TODOS = new HashMap<>();
        TODOS.put("abc123", new JsonObject()
                .put("id", "abc123")
                .put("todo", "something"));
    }

    public JsonObject getTodo(String todoId) {
        return TODOS.get(todoId);
    }
}
