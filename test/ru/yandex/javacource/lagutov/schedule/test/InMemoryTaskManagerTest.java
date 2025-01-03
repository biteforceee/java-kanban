package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void setUp() {
        super.manager = new InMemoryTaskManager();
    }

    @Test
    void addTask() {
        super.addTask();
    }

    @Test
    void addEpic() {
        super.addEpic();
    }

    @Test
    void addSubtask() {
        super.addSubtask();
    }

    @Test
    void getTask() {
        super.getTask();
    }

    @Test
    void getEpic() {
        super.getEpic();
    }

    @Test
    void getSubtask() {
        super.getSubtask();
    }

    @Test
    void getTasks() {
        super.getTasks();
    }

    @Test
    void getEpics() {
        super.getEpics();
    }

    @Test
    void getSubtasks() {
        super.getSubtasks();
    }

    @Test
    void deleteTasks() {
        super.deleteTasks();
    }

    @Test
    void deleteEpics() {
        super.deleteEpics();
    }

    @Test
    void deleteTask() {
        super.deleteTask();
    }

    @Test
    void deleteEpic() {
        super.deleteEpic();
    }

    @Test
    void getEpicSubtasks() {
        super.getEpicSubtasks();
    }

    @Test
    void deleteSubtask() {
        super.deleteSubtask();
    }

    @Test
    void getPrioritisedTasks() {
        class TaskDateComparator implements Comparator<Task> {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getStartTime().compareTo(task2.getStartTime());
            }
        }
        Set <Task> expectedList = new TreeSet<>(new TaskDateComparator());
        Task task1 = new Task("1", "1", Status.NEW, "12:00 01.01.1111","13:00 01.01.1111");
        Task task2 = new Task("2", "2", Status.NEW, "14:00 01.01.7777","15:00 01.01.7777");
        Task task3 = new Task("3", "3", Status.NEW, "10:00 02.01.7778","00:00 03.01.7778");
        Task task4 = new Task("4", "4", Status.NEW, "10:00 01.02.7777","01:00 01.05.7777");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        expectedList.add(task1);
        expectedList.add(task2);
        expectedList.add(task3);
        expectedList.add(task4);
        Assertions.assertEquals(expectedList, manager.getPrioritisedTasks());
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void updateSubtask() {
        super.updateSubtask();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }
}