package ru.yandex.javacource.lagutov.schedule;
import ru.yandex.javacource.lagutov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.manager.*;

public class Main {
    public static void main(String[] args) {
        HistoryManager manager = Managers.getDefaultHistory();

        Task task1 = new Task("задача1","заметка1");

        Task task2 = new Task("задача2","заметка2");
        task2.setStatus(Status.IN_PROGRESS);



        Epic epic1= new Epic("эпик1","1");
        Subtask subtask1 = new Subtask("подзадача1","для эпика1",epic1);
        Subtask subtask2 = new Subtask("подзадача2","для эпика1",epic1);
        Subtask subtask3 = new Subtask("подзадача3","для эпика1",epic1);

        Epic epic2= new Epic("эпик2","2");

        System.out.println("\n------ обращение к задачам :");
        manager.add(subtask1);
        System.out.println(manager.getHistory());
        manager.add(subtask3);
        System.out.println(manager.getHistory());
        manager.add(task2);
        System.out.println(manager.getHistory());
        manager.add(subtask1);
        System.out.println(manager.getHistory());
        manager.add(epic1);
        System.out.println(manager.getHistory());
        manager.add(task2);
        System.out.println(manager.getHistory());
        manager.remove(subtask1.getId());
        System.out.println(manager.getHistory());
    }
}
