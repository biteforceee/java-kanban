package ru.yandex.javacource.lagutov.schedule;
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

        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("task1", "for task1", Status.IN_PROGRESS,
                "10:00 01.01.7777","00:00 01.01.7778");
        taskManager.addTask(task);
        task.setNote("task");
        taskManager.updateTask(task);

        System.out.println(taskManager.getPrioritisedTasks());

        taskManager.addTask(new Task("task2", "for task2"));
        System.out.println(taskManager.getPrioritisedTasks());
        taskManager.addEpic(new Epic("epic1", "for epic1"));
        taskManager.addEpic(new Epic("epic2", "for epic2"));
        Subtask subtask = new Subtask("subtask1", "for epic1");
        taskManager.addSubtask(subtask, 3);
        subtask.setStatus(Status.DONE);
        subtask.setNote("epic1");
        subtask.setStartTime("10:00 01.01.7779");
        subtask.setDuration(Duration.between(subtask.getStartTime(),
                LocalDateTime.parse("00:00 01.01.7780", dateFormat)));
        taskManager.updateSubtask(subtask);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getPrioritisedTasks());
    }
}
