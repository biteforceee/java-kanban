package ru.yandex.javacource.lagutov.schedule;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.manager.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        File file1 = File.createTempFile("test","csv");
        File file2 = File.createTempFile("test2","csv");
        //File testFile = new File("D:\\YaProjects\\tmpfiles\\test.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file1);
        manager.addTask(new Task("task1", "for task1"));
        manager.addTask(new Task("task2", "for task2"));

        manager.addEpic(new Epic("epic1", "for epic1"));
        manager.addEpic(new Epic("epic2", "for epic2"));
        Subtask subtask = new Subtask("subtask1", "for epic1");
        manager.addSubtask(subtask, 3);
        subtask.setStatus(Status.DONE);
        subtask.setNote("epic1");
        manager.updateSubtask(subtask);

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file1);
        System.out.println(manager2.getEpics());

        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(new Task("task1", "for task1"));
        taskManager.addTask(new Task("task2", "for task2"));
    }
}
