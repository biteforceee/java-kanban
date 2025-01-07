package ru.yandex.javacource.lagutov.schedule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.DurationAdapter;
import ru.yandex.javacource.lagutov.schedule.manager.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.manager.*;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
//        File file1 = File.createTempFile("test","csv");
//        //File file2 = File.createTempFile("test2","csv");
//        //File testFile = new File("D:\\YaProjects\\tmpfiles\\test.csv");
//        FileBackedTaskManager manager = new FileBackedTaskManager(file1);
//        manager.addTask(new Task("task1", "for task1"));
//        manager.addTask(new Task("task2", "for task2"));
//
//        manager.addEpic(new Epic("epic1", "for epic1"));
//        manager.addEpic(new Epic("epic2", "for epic2"));
//        Subtask subtask = new Subtask("subtask1", "for epic1");
//        manager.addSubtask(subtask, 3);
//        subtask.setStatus(Status.DONE);
//        subtask.setNote("epic1");
//        subtask.setStartTime(LocalDateTime.parse("10:00 01.01.7777",dateFormat));
//        subtask.setEndTime(LocalDateTime.parse("00:00 01.01.7778",dateFormat));
//        manager.updateSubtask(subtask);

        //FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file1);
        //System.out.println(manager2.getEpics());
//        TaskManager taskManager = Managers.getDefault();
//        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
//                "10:00 01.01.7777","00:00 01.01.7778");
//        taskManager.addTask(task);
//        task.setNote("task");
//        taskManager.updateTask(task);
//
//        System.out.println(taskManager.getPrioritisedTasks());
//
//        taskManager.addTask(new Task("task2", "for task2"));
//        System.out.println(taskManager.getPrioritisedTasks());
//        taskManager.addEpic(new Epic("epic1", "for epic1"));
//        taskManager.addEpic(new Epic("epic2", "for epic2"));
//        Subtask subtask = new Subtask("subtask1", "for epic1");
//        taskManager.addSubtask(subtask, 3);
//        subtask.setStatus(Status.DONE);
//        subtask.setNote("epic1");
//        subtask.setStartTime("10:00 01.01.7779");
//        subtask.setDuration(Duration.between(subtask.getStartTime(),
//                LocalDateTime.parse("00:00 01.01.7780", dateFormat)));
//        taskManager.updateSubtask(subtask);
//        System.out.println(taskManager.getEpics());
//        System.out.println(taskManager.getPrioritisedTasks());
        HttpTaskServer server = new HttpTaskServer();
        server.start();
        TaskManager manager = Managers.getDefault();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        Task task1 = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");
        manager.addTask(task1);
        Task task2 = new Task("jump every day", "30 iterations");
        manager.addTask(task2);
        Epic epic1 = new Epic("Съездить в Москву", "обязательно до лета");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask(2, "1", "1", Status.NEW, epic1.getId(),
                "12:00 01.01.1111","13:00 01.01.1111");
        manager.addSubtask(subtask11, epic1.getId());
        Subtask subtask12 = new Subtask(2, "1", "1", Status.NEW, epic1.getId(),
                "12:00 01.01.2222","13:00 01.01.2222");
        manager.addSubtask(subtask12, epic1.getId());
        Epic epic2 = new Epic("посмотреть кино", "обязательно до конца месяца");
        manager.addEpic(epic2);
        Subtask subtask21 = new Subtask("найти кино", "в хорошем качестве");
        manager.addSubtask(subtask21, epic2.getId());

        System.out.println(gson.toJson(task1));
        System.out.println(gson.toJson(epic1));
        System.out.println(epic2);
        Epic epic3 = gson.fromJson(gson.toJson(epic1), Epic.class);
        System.out.println(epic3);
        System.out.println(gson.toJson(task2));
        System.out.println(gson.toJson(epic1));
        System.out.println(gson.toJson(subtask11));
        System.out.println(gson.toJson(subtask12));
        System.out.println(gson.toJson(epic2));
        System.out.println(gson.toJson(subtask21));

        String jsonString = gson.toJson(subtask11);
        Subtask subtaskFromJson = gson.fromJson(jsonString, Subtask.class);
        System.out.println(subtaskFromJson);
    }
}