package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.Managers;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    /*Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");*/
    @Test
    void addTask() {
        Task task= new Task("","");
        taskManager.addTask(task);
        Assertions.assertEquals(1,taskManager.getTasks().size());
    }

    @Test
    void addEpic() {
        Epic epic= new Epic("","");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1,taskManager.getEpics().size());
    }

    @Test
    void addSubtask() {
        Subtask subtask= new Subtask("","");
        taskManager.addSubtask(subtask,0);
        Assertions.assertEquals(1,taskManager.getSubtasks().size());
    }

    @Test
    void getTask() {
        Task actual_task= new Task("","");
        taskManager.addTask(actual_task);
        int id = actual_task.getId();
        Task expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getTask(id));
    }

    @Test
    void getEpic() {
        Epic actual_task= new Epic("","");
        taskManager.addEpic(actual_task);
        int id = actual_task.getId();
        Epic expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getEpic(id));
    }

    @Test
    void getSubtask() {
        Subtask actual_task= new Subtask("","");
        taskManager.addSubtask(actual_task,-1);
        int id = actual_task.getId();
        Subtask expected_task = actual_task;
        Assertions.assertEquals(expected_task,taskManager.getSubtask(id));
    }

    @Test
    void getTasks() {
        Task actual_task= new Task("","");
        taskManager.addTask(actual_task);
        ArrayList<Task> expected_task = new ArrayList<>();
        expected_task.add(actual_task);
        Assertions.assertEquals(expected_task,taskManager.getTasks());
    }

    @Test
    void getEpics() {
        Epic actual_task= new Epic("","");
        taskManager.addEpic(actual_task);
        ArrayList<Epic> expected_task = new ArrayList<>();
        expected_task.add(actual_task);
        Assertions.assertEquals(expected_task,taskManager.getEpics());
    }

    @Test
    void getSubtasks() {
        Subtask actual_task= new Subtask("","");
        taskManager.addSubtask(actual_task,-1);
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
        Epic actual_task= new Epic("","");
        taskManager.addTask(actual_task);
        taskManager.deleteEpics();
        Assertions.assertEquals(0,taskManager.getEpics().size());
    }

    @Test
    void deleteTask() {
        Task task1= new Epic("","");
        Task task2= new Epic("","");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTask(task2.getId());
        ArrayList<Task> arrl= new ArrayList<>();
        arrl.add(task1);
        Assertions.assertEquals(arrl,taskManager.getTasks());
    }

    @Test
    void deleteEpic() {
        Epic epic1= new Epic("","");
        Epic epic2= new Epic("","");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpic(epic2);
        ArrayList<Epic> arrl= new ArrayList<>();
        arrl.add(epic1);
        Assertions.assertEquals(arrl,taskManager.getEpics());
    }
}