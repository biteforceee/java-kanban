package ru.yandex.javacource.lagutov.schedule.manager.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.DurationAdapter;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                getHistory(exchange);
                return;
            default:
                BaseHttpHandler.writeResponse(exchange, "Not Found", 404);
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        String history = gson.toJson(manager.getHistory(), List.class);
        BaseHttpHandler.writeResponse(exchange, history, 200);
        exchange.close();
    }

//
//    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
//        byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
//        exchange.sendResponseHeaders(responseCode, bytes.length);
//        try (OutputStream outputStream = exchange.getResponseBody()) {
//            outputStream.write(bytes);
//        }
//        exchange.close();
//    }
//
//    private String readText(HttpExchange exchange) throws IOException {
//        return new String(exchange.getRequestBody().readAllBytes());
//    }
}