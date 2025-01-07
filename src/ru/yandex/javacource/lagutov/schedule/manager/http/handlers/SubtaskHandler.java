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

public class SubtaskHandler implements HttpHandler {

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public final TaskManager manager;

    public SubtaskHandler(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        switch (method) {
            case "GET":
                if (splitPath.length == 2) {
                    getSubtasks(exchange);
                } else {
                    getSubtask(exchange);
                }
                return;
            case "POST":
                if (splitPath.length > 2) {
                    updateSubtask(exchange);
                } else {
                    addSubtask(exchange);
                }
                return;
            case "DELETE":
                deleteSubtask(exchange);
                return;
            default:
                BaseHttpHandler.writeResponse(exchange, "Not Found", 404);
        }
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = BaseHttpHandler.getIdFromURI(exchange);
        if (id.isEmpty()) {
            BaseHttpHandler.sendNotFound(exchange, "Not found");
            return;
        }
        if (manager.getSubtask(id.get()) == null) {
            BaseHttpHandler.writeResponse(exchange, "Not found", 404);
            return;
        }
        String responseString = gson.toJson(manager.getSubtask(id.get()));
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили подзадачу с id = " + id);
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getSubtasks());
        BaseHttpHandler.writeResponse(exchange, responseString, 200);
        System.out.println("Получили все подзадачи");
    }

    private void addSubtask(HttpExchange exchange) throws IOException {
        String body = BaseHttpHandler.readText(exchange);
        Subtask task = gson.fromJson(body, Subtask.class);

        manager.addSubtask(task, task.getEpicId());
        BaseHttpHandler.writeResponse(exchange, "Подзадача создана успешно", 201);
        System.out.println("Создали подзадачу id = " + task.getId());
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        String body = BaseHttpHandler.readText(exchange);
        Subtask task = gson.fromJson(body, Subtask.class);
        manager.deleteSubtask(task.getId());
        BaseHttpHandler.writeResponse(exchange, "Подзадача удалена", 201);
        System.out.println("Удалили подзадачу id = " + task.getId());
    }

    private void updateSubtask(HttpExchange exchange) throws IOException {
        String body = BaseHttpHandler.readText(exchange);
        Subtask task = gson.fromJson(body, Subtask.class);
        try {
            manager.updateSubtask(task);
            ///
            Epic epic = manager.getEpic(task.getEpicId());
            exchange.getResponseBody();
            BaseHttpHandler.writeResponse(exchange, "Подзадача обнавлена", 201);
            ///
            BaseHttpHandler.writeResponse(exchange, "Подзадача обнавлена", 201);

            System.out.println("Обновили подзадачу id = " + task.getId());
        } catch (TaskValidationException e) {
            BaseHttpHandler.writeResponse(exchange, "Задача имеет пересечение", 406);
        }
    }
}
