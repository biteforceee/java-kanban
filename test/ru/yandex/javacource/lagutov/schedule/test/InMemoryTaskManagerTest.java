package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.lagutov.schedule.manager.Managers;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addTask() {
        Task task = new Task("","");
        taskManager.addTask(task);
        Assertions.assertEquals(1,taskManager.getTasks().size());
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("","");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1,taskManager.getEpics().size());
    }

    @Test
    void addSubtask() {
        Subtask subtask = new Subtask("","");
        Epic epic = new Epic("","");
        taskManager.addEpic(epic);
        subtask.setEpicID(epic.getId());
        taskManager.addSubtask(subtask, subtask.getEpicId());
        Assertions.assertEquals(1,taskManager.getSubtasks().size());
    }

    @Test
    void getTask() {
        Task actual_task = new Task("","");
        taskManager.addTask(actual_task);
        int id = actual_task.getId();
        Task expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getTask(id));
    }

    @Test
    void getEpic() {
        Epic actual_task = new Epic("","");
        taskManager.addEpic(actual_task);
        int id = actual_task.getId();
        Epic expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getEpic(id));
    }

    @Test
    void getSubtask() {
        Epic epic = new Epic("","");
        taskManager.addEpic(epic);
        Subtask actual_task = new Subtask("","");
        taskManager.addSubtask(actual_task,epic.getId());
        int id = actual_task.getId();
        Subtask expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getSubtask(id));
    }

    @Test
    void getTasks() {
        Task actual_task = new Task("","");
        taskManager.addTask(actual_task);
        ArrayList<Task> expected_task = new ArrayList<>();
        expected_task.add(actual_task);
        Assertions.assertEquals(expected_task,taskManager.getTasks());
    }

    @Test
    void getEpics() {
        Epic actual_task = new Epic("","");
        taskManager.addEpic(actual_task);
        ArrayList<Epic> expected_task = new ArrayList<>();
        expected_task.add(actual_task);
        Assertions.assertEquals(expected_task,taskManager.getEpics());
    }

    @Test
    void getSubtasks() {
        Epic epic = new Epic("","");
        taskManager.addEpic(epic);
        Subtask actual_task = new Subtask("","");
        taskManager.addSubtask(actual_task,epic.getId());
        ArrayList<Subtask> expected_task = new ArrayList<>();
        expected_task.add(actual_task);
        Assertions.assertEquals(expected_task,taskManager.getSubtasks());
    }

    @Test
    void deleteTasks() {
        Task actual_task= new Task("","");
        taskManager.addTask(actual_task);
        taskManager.deleteTasks();
        Assertions.assertEquals(0,taskManager.getTasks().size());
    }

    @Test
    void deleteEpics() {
        Epic actual_task = new Epic("","");
        taskManager.addTask(actual_task);
        taskManager.deleteEpics();
        Assertions.assertEquals(0,taskManager.getEpics().size());
    }

    @Test
    void deleteTask() {
        Task task1 = new Epic("","");
        Task task2 = new Epic("","");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTask(task2.getId());
        ArrayList<Task> arrl = new ArrayList<>();
        arrl.add(task1);
        Assertions.assertEquals(arrl,taskManager.getTasks());
    }

    @Test
    void deleteEpic() {
        Epic epic1 = new Epic("","");
        Epic epic2 = new Epic("","");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpic(epic2.getId());
        ArrayList<Epic> arrl = new ArrayList<>();
        arrl.add(epic1);
        Assertions.assertEquals(arrl,taskManager.getEpics());
    }

    @Test
    void getEpicSubtasks() {
        List<Integer> list = new ArrayList<>();
        Epic epic = new Epic("title","note");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("","");
        Subtask subtask2 = new Subtask("","");
        Subtask subtask3 = new Subtask("","");

        list.add(subtask1.getId());
        epic.addSubtaskId(subtask1.getId());

        list.add(subtask2.getId());
        epic.addSubtaskId(subtask2.getId());

        list.add(subtask3.getId());
        epic.addSubtaskId(subtask3.getId());

        epic.removeSubtaskIds(subtask2.getId());
        list.remove(1);

        Assertions.assertEquals(list,taskManager.getEpicSubtasks(epic));
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("title","note");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("","");
        taskManager.addSubtask(subtask, epic.getId());
        taskManager.deleteSubtask(subtask.getId());

        Assertions.assertEquals(0,taskManager.getEpicSubtasks(epic).size());
    }
}