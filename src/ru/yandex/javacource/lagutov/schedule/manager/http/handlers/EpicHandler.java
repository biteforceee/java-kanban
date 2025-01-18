package ru.yandex.javacource.lagutov.schedule.manager.http.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.lagutov.schedule.manager.*;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.DurationAdapter;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.lagutov.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Optional;

public class EpicHandler implements HttpHandler {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TaskManager manager;

    public EpicHandler(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        switch (method) {
            case "GET":
                if (splitPath.length == 3) {
                    getEpic(exchange);
                } else if (splitPath.length == 4) {
                    getEpicSubtasks(exchange);
                } else {
                    getEpics(exchange);
                }
                return;
            case "POST":
                addEpic(exchange);
                return;
            case "DELETE":
                deleteEpic(exchange);
                return;
            default:
                BaseHttpHandler.sendNotFound(exchange, "Not found");
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> id = BaseHttpHandler.getIdFromURI(exchange);
        if (id.isEmpty() || manager.getEpic(id.get()) == null) {
            BaseHttpHandler.sendNotFound(exchange, "Not found");
            return;
        }
        String responseString = gson.toJson(manager.getEpic(id.get()));
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили epic с id = " + id);
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> id = BaseHttpHandler.getIdFromURI(exchange);
        if (id.isEmpty()) {
            BaseHttpHandler.sendNotFound(exchange, "Not found");
            return;
        }
        if (manager.getEpic(id.get()) == null) {
            BaseHttpHandler.writeResponse(exchange, "Несуществует epic с id=" + id.get(), 404);
            return;
        }
        String responseString = gson.toJson(manager.getEpicSubtasks(manager.getEpic(id.get())));
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили epic с id = " + id);
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getEpics());
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили все epic-и");
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        String body = BaseHttpHandler.readText(exchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            BaseHttpHandler.writeResponse(exchange, "Not Acceptable", 406);
            System.out.println(1);
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic task = gson.fromJson(jsonObject, Epic.class);

        manager.addEpic(task);
        BaseHttpHandler.writeResponse(exchange, "Epic создана успешно", 201);
        System.out.println("Создали epic id = " + task.getId());
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        String body = BaseHttpHandler.readText(exchange);
        Epic task = gson.fromJson(body, Epic.class);
        manager.deleteEpic(task.getId());
        BaseHttpHandler.writeResponse(exchange, "Epic удален", 201);
        System.out.println("Удалили epic id = " + task.getId());
    }
}
