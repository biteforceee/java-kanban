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
//        InMemoryTaskManager taskManager = new InMemoryTaskManager();
//        Task a = new Task("title1","note1");
//        Task b = new Task("title2","note2");
//        taskManager.addTask(a);
//        a.setStatus(Status.DONE);
//        taskManager.addTask(b);
//        System.out.println(taskManager.getTasks());
//        taskManager.deleteTask(1);
//        System.out.println(taskManager.getTasks());
//        taskManager.deleteTasks();
//        System.out.println(taskManager.getTasks());
//        ///////////
//        Epic epic1 = new Epic("title1","note1");
//        taskManager.addEpic(epic1);
//        Subtask aa = new Subtask("subTitle1","subNote1");
//        taskManager.addSubtask(aa,epic1);
//        Subtask bb = new Subtask("subTitle2","subNote2");
//        taskManager.addSubtask(bb,epic1);
//        aa.setStatus(Status.DONE);
//        taskManager.updateSubtask(aa);
//        taskManager.updateEpic(epic1);
//        System.out.println("//////////////////////");
//        for(Epic epic :taskManager.getEpics()){
//            System.out.println(epic.toString());
//            System.out.println(epic.getSubtasksIds());
//        }
//        Epic epic2 =new Epic("title2","note2");
//        taskManager.addEpic(epic2);
//        Subtask cc = new Subtask("subTitle3","subNote3");
//        taskManager.addSubtask(cc,epic2);
//        System.out.println("//////////////////////");
//        for(Epic epic :taskManager.getEpics()){
//            System.out.println(epic.toString());
//            System.out.println(epic.getSubtasksIds());
//        }
//        //taskManager.deleteEpic(3);
//        taskManager.deleteEpic(epic2);
//        System.out.println("//////////////////////");
//        for(Epic epic :taskManager.getEpics()){
//            System.out.println(epic.toString());
//            System.out.println(epic.getSubtasksIds());
//        }
//        taskManager.deleteEpics();
//        System.out.println("//////////////////////");
//        for(Epic epic :taskManager.getEpics()){
//            System.out.println(epic.toString());
//            System.out.println(epic.getSubtasksIds());
//        }
        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task("Task1", "TaskNote1")); /*1*/
        taskManager.addTask(new Task("Task2", "TaskNote2")); /*2*/

        taskManager.addEpic(new Epic("Epic1", "Epic1 desc")); /*3*/
        taskManager.addSubtask(new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc"), 3); /*4*/

        taskManager.addEpic(new Epic("Epic2", "Epic2 desc")); /*5*/
        taskManager.addSubtask(new Subtask("Subtask1 Epic2", "Subtask1 Epic2 desc"), 5); /*6*/
        taskManager.addSubtask(new Subtask("Subtask2 Epic2", "Subtask2 Epic2 desc"), 5); /*7*/

        System.out.println("=====CREATED=====");
        System.out.println("TASKS\n" + taskManager.getTasks());
        System.out.println("EPICS\n" + taskManager.getEpics());
        System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(2);

        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getEpic(5);

        taskManager.getSubtask(6);
        taskManager.getSubtask(7);
        taskManager.getSubtask(4);
        taskManager.getSubtask(4);

        taskManager.getTask(1);
        taskManager.getTask(2);

        System.out.println("=====HISTORY=====");
        System.out.println(taskManager.getHistory());

        taskManager.updateTask(new Task("Task1", "TaskDesc1"));
        taskManager.updateSubtask(new Subtask("Subtask1 Epic2", "Subtask1 Epic2 desc"));

        System.out.println("=====UPDATED=====");
        System.out.println("TASKS\n" + taskManager.getTasks());
        System.out.println("EPICS\n" + taskManager.getEpics());
        System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

        taskManager.deleteTask(1);
        taskManager.deleteSubtask(7);

        System.out.println("=====REMOVE-1 (task+subtask)=====");
        System.out.println("TASKS\n" + taskManager.getTasks());
        System.out.println("EPICS\n" + taskManager.getEpics());
        System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

        taskManager.deleteTask(2);
        System.out.println("=====HISTORY REMOVE-1 (task+subtask)=====");
        System.out.println(taskManager.getHistory());

        taskManager.deleteSubtask(6);


        System.out.println("=====REMOVE-2 (subtask)=====");
        System.out.println("TASKS\n" + taskManager.getTasks());
        System.out.println("EPICS\n" + taskManager.getEpics());
        System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

        System.out.println("=====HISTORY REMOVE-2 (subtask)=====");
        System.out.println(taskManager.getHistory());

        taskManager.deleteEpics();

        System.out.println("=====CLEAR (epics)=====");
        System.out.println("TASKS\n" + taskManager.getTasks());
        System.out.println("EPICS\n" + taskManager.getEpics());
        System.out.println("SUBTASKS\n" + taskManager.getSubtasks());

        System.out.println("=====HISTORY CLEAR (epics)=====");
        System.out.println(taskManager.getHistory());
    }
}
