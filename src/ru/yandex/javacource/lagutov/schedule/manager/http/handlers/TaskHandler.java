package ru.yandex.javacource.lagutov.schedule.manager.http.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.lagutov.schedule.manager.*;
import ru.yandex.javacource.lagutov.schedule.manager.exeptions.TaskValidationException;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.DurationAdapter;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.lagutov.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Optional;

import static ru.yandex.javacource.lagutov.schedule.manager.http.handlers.BaseHttpHandler.*;

public class TaskHandler implements HttpHandler {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public final TaskManager manager;

    public TaskHandler(TaskManager manager) throws IOException {
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
                    getTask(exchange);
                } else if (splitPath.length == 2) {
                    getTasks(exchange);
                }
                return;
            case "POST":
                if (splitPath.length == 3) {
                    updateTask(exchange);
                } else if (splitPath.length == 2) {
                    addTask(exchange);
                }
                return;
            case "DELETE":
                deleteTask(exchange);
                return;
            default:
                BaseHttpHandler.sendNotFound(exchange, "Not found");
        }
    }

    private void getTask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = BaseHttpHandler.getIdFromURI(exchange);
        if (id.isEmpty() || manager.getTask(id.get()) == null) {
            BaseHttpHandler.sendNotFound(exchange, "Not found");
            return;
        }
        if (manager.getTask(id.get()) != null){
            String responseString = gson.toJson(manager.getTask(id.get()));
            BaseHttpHandler.writeResponse(exchange, responseString, 200);
        }
        System.out.println("Получили задачу с id = " + id);
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getTasks());
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили все задачи" + responseString);
    }

    private void addTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        if(!jsonElement.isJsonObject()) {
            BaseHttpHandler.writeResponse(exchange, "Not Acceptable", 406);
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task = gson.fromJson(jsonObject, Task.class);
        if (task == null){
            BaseHttpHandler.writeResponse(exchange, "Not Acceptable", 406);
            return;
        }
        try {
            manager.addTask(task);
            BaseHttpHandler.writeResponse(exchange, "Задача создана успешно", 201);
            System.out.println("Создали задачу id = " + task.getId());
        } catch (TaskValidationException e) {
            BaseHttpHandler.sendHasInteractions(exchange, "Задача имеет пересечение");
        }
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        if(!jsonElement.isJsonObject()) {
            BaseHttpHandler.writeResponse(exchange, "Not Acceptable", 406);
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task = gson.fromJson(jsonObject, Task.class);
        if (task == null){
            BaseHttpHandler.writeResponse(exchange, "Not Acceptable", 406);
            return;
        }
        try {
            manager.updateTask(task);
            BaseHttpHandler.writeResponse(exchange, "Задача обнавлена", 201);
            System.out.println("Обновили задачу id = " + task.getId());
        } catch (TaskValidationException e) {
            BaseHttpHandler.sendHasInteractions(exchange, "Задача имеет пересечение");
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = BaseHttpHandler.getIdFromURI(exchange);
        if (id.isPresent()) {
            if (manager.getTask(id.get()) != null) {
                manager.deleteTask(id.get());
                BaseHttpHandler.writeResponse(exchange, "Задача удалена", 201);
                System.out.println("Удалили задачу id = " + id.get());
            }
        }
    }
}
