package ru.yandex.javacource.lagutov.schedule.test;

import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.*;
import ru.yandex.javacource.lagutov.schedule.manager.*;
import ru.yandex.javacource.lagutov.schedule.task.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class HttpTaskServerTest {
    private static HttpTaskServer taskServer;

    private InMemoryTaskManager manager = new InMemoryTaskManager();
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private static final String TASK_BASE_URL = "http://localhost:8080/tasks/";
    private static final String EPIC_BASE_URL = "http://localhost:8080/epics/";
    private static final String SUBTASK_BASE_URL = "http://localhost:8080/subtasks/";


    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteEpics();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    private void resetServerState() {
        HttpClient client = HttpClient.newHttpClient();
        try {
            client.send(
                    HttpRequest.newBuilder().uri(URI.create(TASK_BASE_URL)).DELETE().build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            client.send(
                    HttpRequest.newBuilder().uri(URI.create(EPIC_BASE_URL)).DELETE().build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            client.send(
                    HttpRequest.newBuilder().uri(URI.create(SUBTASK_BASE_URL)).DELETE().build(),
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
//    @AfterEach
//    void resetServer() {
//        HttpClient client = HttpClient.newHttpClient();
//        URI url = URI.create(TASK_BASE_URL);
//        try {
//            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
//            client.send(request, HttpResponse.BodyHandlers.ofString());
//            url = URI.create(EPIC_BASE_URL);
//            request = HttpRequest.newBuilder().uri(url).DELETE().build();
//            client.send(request, HttpResponse.BodyHandlers.ofString());
//            url = URI.create(SUBTASK_BASE_URL);
//            request = HttpRequest.newBuilder().uri(url).DELETE().build();
//            client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    @Order(15)
    void getTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(15)
    void shouldGetEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(14)
    void shouldGetSubtasksTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST");
            if (postResponse.statusCode() == 201) {
                int epicId = epic.getId();//так как первый элемент который добавил manager
                //epic.setId(epicId);
                Subtask subtask = new Subtask(2, "1", "1", Status.NEW, epic.getId());
                url = URI.create(SUBTASK_BASE_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
                assertEquals(1, arrayTasks.size());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void shouldGetTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        //manager.addTask(task);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                int id = 1;
                task.setId(id);
                //int id = task.getId();
                url = URI.create(TASK_BASE_URL + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Task responseTask = gson.fromJson(response.body(), Task.class);
                assertEquals(task, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void shouldGetEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                //int id = 2;
                //epic.setId(id);
                int id = epic.getId();
                url = URI.create(EPIC_BASE_URL + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Epic responseTask = gson.fromJson(response.body(), Epic.class);
                assertEquals(epic, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    void shouldGetSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                //int epicId = 4;
                //epic.setId(epicId);
                Subtask subtask = new Subtask(2, "1", "1", Status.NEW, epic.getId());//"12:00 01.01.1111","13:00 01.01.1111"
                manager.addSubtask(subtask, epic.getId());
                url = URI.create(SUBTASK_BASE_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                assertEquals(201, postResponse.statusCode());

                if (postResponse.statusCode() == 201) {
                    //int id = 2;
                    //subtask.setId(id);
                    int id = subtask.getId();
                    url = URI.create(SUBTASK_BASE_URL + id);
                    request = HttpRequest.newBuilder().uri(url).GET().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(200, response.statusCode());
                    Subtask responseTask = gson.fromJson(response.body(), Subtask.class);
                    assertEquals(subtask, responseTask);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(13)
    void updateTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        manager.addTask(task);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (postResponse.statusCode() == 201) {
                //int id = 1;
                //task.setId(1);
                int id = task.getId();
                task.setTitle("task test");
                task.setStatus(Status.IN_PROGRESS);
                url = URI.create(TASK_BASE_URL + id);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());

                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Task responseTask = gson.fromJson(response.body(), Task.class);
                assertEquals(task, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(12)
    void shouldUpdateEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        URI url2 = URI.create(SUBTASK_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (postResponse.statusCode() == 201) {
                //int id = 3;
                //epic.setId(id);
                //manager.addEpic(epic);
                int id = epic.getId();
                url = URI.create(EPIC_BASE_URL + id);
//
//                request = HttpRequest.newBuilder()
//                        .uri(url)
//                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
//                        .build();
//                client.send(request, HttpResponse.BodyHandlers.ofString());
                Subtask subtask = new Subtask(2, "1", "1", Status.IN_PROGRESS, epic.getId(),
                        "12:00 01.01.1111","13:00 01.01.1111");
                //"12:00 01.01.1111","13:00 01.01.1111"

                manager.addSubtask(subtask, epic.getId());
                request = HttpRequest.newBuilder()
                        .uri(url2)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());


                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Epic responseTask = gson.fromJson(response.body(), Epic.class);
                assertEquals(manager.getEpic(1), responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    void shouldUpdateSubtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                //int epicId = 9;
                //epic.setId(epicId);

                Subtask subtask = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                        ,"12:00 01.01.1111","13:00 01.01.1111");
                url = URI.create(SUBTASK_BASE_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                manager.addSubtask(subtask, epic.getId());
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (postResponse.statusCode() == 201) {
                    int id = subtask.getId();
                    subtask.setStatus(Status.IN_PROGRESS);
                    //manager.updateSubtask(subtask);
                    url = URI.create(SUBTASK_BASE_URL + id);
                    request = HttpRequest.newBuilder()
                            .uri(url)
                            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                            .build();
                    client.send(request, HttpResponse.BodyHandlers.ofString());

                    url = URI.create(SUBTASK_BASE_URL + id);
                    request = HttpRequest.newBuilder().uri(url).GET().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(200, response.statusCode());
                    Subtask responseTask = gson.fromJson(response.body(), Subtask.class);
                    assertEquals(subtask, responseTask);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(10)
    void shouldDeleteTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(9)
    void shouldDeleteEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    void shouldDeleteSubtasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                int epicId = 1;
                epic.setId(epicId);
                Subtask subtask = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                        ,"12:00 01.01.1111","13:00 01.01.1111");
                url = URI.create(SUBTASK_BASE_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
                request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(204, response.statusCode());
                request = HttpRequest.newBuilder().uri(url).GET().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
                assertEquals(0, arrayTasks.size());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    void shouldDeleteTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        manager.addTask(task);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id = task.getId();//1;
            url = URI.create(TASK_BASE_URL + id);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Not found", response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    void shouldDeleteEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                int id = epic.getId();
                url = URI.create(EPIC_BASE_URL + id);
                request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(201, response.statusCode());

                request = HttpRequest.newBuilder().uri(url).GET().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals("Not found", response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    void shouldDeleteSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic = new Epic("epic1", "for epic1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        manager.addEpic(epic);
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                Subtask subtask = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                        ,"12:00 01.01.1111","13:00 01.01.1111");
                url = URI.create(SUBTASK_BASE_URL);
                manager.addSubtask(subtask, epic.getId());
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                assertEquals(201, postResponse.statusCode());
                if (postResponse.statusCode() == 201) {
                    //int id = 2;
                    //subtask.setId(id);
                    int id = subtask.getId();
                    url = URI.create(SUBTASK_BASE_URL + id);

                    request = HttpRequest.newBuilder()
                            .uri(url)
                            .DELETE()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(201, response.statusCode());

                    request = HttpRequest.newBuilder().uri(url).GET().build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals("Not found", response.body());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
