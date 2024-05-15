package ru.yandex.javacource.lagutov.schedule;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;
import ru.yandex.javacource.lagutov.schedule.task.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task a = new Task("title1","note1");
        Task b = new Task("title2","note2");
        taskManager.addTask(a);
        a.setStatus(Status.DONE);
        taskManager.addTask(b);
        System.out.println(taskManager.getTasks());
        taskManager.deleteTask(1);
        System.out.println(taskManager.getTasks());
        taskManager.deleteTasks();
        System.out.println(taskManager.getTasks());
        ///////////
        Epic epic1 = new Epic("title1","note1");
        taskManager.addEpic(epic1);
        Subtask aa = new Subtask("subTitle1","subNote1");
        taskManager.addSubtask(aa,epic1);
        Subtask bb = new Subtask("subTitle2","subNote2");
        taskManager.addSubtask(bb,epic1);
        aa.setStatus(Status.DONE);
        taskManager.updateSubtask(aa);
        taskManager.updateEpic(epic1);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasksIds());
        }
        Epic epic2 =new Epic("title2","note2");
        taskManager.addEpic(epic2);
        Subtask cc = new Subtask("subTitle3","subNote3");
        taskManager.addSubtask(cc,epic2);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasksIds());
        }
        //taskManager.deleteEpic(3);
        taskManager.deleteEpic(epic2);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasksIds());
        }
        taskManager.deleteEpics();
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasksIds());
        }
    }
}
