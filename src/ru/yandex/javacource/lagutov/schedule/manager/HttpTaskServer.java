package ru.yandex.javacource.lagutov.schedule.manager;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import ru.yandex.javacource.lagutov.schedule.manager.http.handlers.*;

public class HttpTaskServer {

    private static final int PORT = 8080;

    private static final InMemoryTaskManager manager = new InMemoryTaskManager();

    static HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start() {
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(1);
    }
}
