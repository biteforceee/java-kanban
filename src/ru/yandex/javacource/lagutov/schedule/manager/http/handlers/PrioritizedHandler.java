package ru.yandex.javacource.lagutov.schedule.manager.http.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.lagutov.schedule.manager.*;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.DurationAdapter;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.LocalDateTimeAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler implements HttpHandler {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public final TaskManager manager;

    public PrioritizedHandler(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getPrioritisedTasks(exchange);
                return;
            default:
                BaseHttpHandler.writeResponse(exchange, "Not Found", 404);
        }
    }

    private void getPrioritisedTasks(HttpExchange exchange) throws IOException {
        //String body = readText(exchange);
        String prioritized = gson.toJson(manager.getPrioritisedTasks(), Set.class);
        BaseHttpHandler.writeResponse(exchange, prioritized, 200);
        exchange.close();
    }
}