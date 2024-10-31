package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import ru.yandex.javacource.lagutov.schedule.manager.TaskManager;
import ru.yandex.javacource.lagutov.schedule.task.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T manager;
    private final Task task1 = new Task("Task1", "Desc1", Status.NEW, "12:00 01.01.1111","13:00 01.01.1111");
    private final Task task2 = new Task("Task2", "Desc2", Status.NEW, "14:00 01.01.7777","15:00 01.01.7777");
    private final Epic epic1 = new Epic("Epic1", "for Epic1");
    private final Epic epic2 = new Epic("Epic2", "for Epic2");

    @Test
    void addTask() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            manager.addTask(null);
        });
        Task task = new Task("","", Status.IN_PROGRESS, "01:01 01.01.2024", "01:02 01.01.2024");
        manager.addTask(task);
        Assertions.assertNotNull(manager.getTask(task.getId()));
        Assertions.assertEquals(1,manager.getTasks().size());
        Assertions.assertEquals(1,manager.getPrioritisedTasks().size());
    }

    @Test
    void addEpic() {
        Assertions.assertThrows(NullPointerException.class, () -> manager.addEpic(null));
        int localEpic = manager.addEpic(epic1);
        Assertions.assertNotNull(manager.getEpic(localEpic));
        Assertions.assertEquals(epic1, manager.getEpic(localEpic));
    }

    @Test
    void addSubtask() {
        Assertions.assertThrows(NullPointerException.class, () -> manager.addSubtask(null, -1));
        Subtask subtask = new Subtask("","");
        subtask.setStartTime("01:01 01.01.2024");
        subtask.setEndTime("01:02 01.01.2024");
        Epic epic = new Epic("","");
        manager.addEpic(epic);
        subtask.setEpicID(epic.getId());
        manager.addSubtask(subtask, epic.getId());
        Assertions.assertEquals(1, manager.getPrioritisedTasks().size());
        Assertions.assertEquals(1, manager.getSubtasks().size());
        Assertions.assertEquals(epic.getDuration(), subtask.getDuration());
    }

    @Test
    void deleteTasks() {
        List<Task> expectation = List.of();

        manager.addTask(task1);
        manager.addTask(task2);
        manager.deleteTasks();

        Assertions.assertEquals(expectation, manager.getTasks());
    }

    @Test
    void deleteEpics() {
        List<Task> expectation = List.of();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.deleteEpics();

        Assertions.assertEquals(expectation, manager.getEpics());
    }

    @Test
    void getTasks() {
        Assertions.assertEquals(0, manager.getTasks().size());
        manager.addTask(task1);
        manager.addTask(task2);

        Assertions.assertEquals(2, manager.getTasks().size());
    }

    @Test
    void getEpics() {
        Assertions.assertEquals(0, manager.getEpics().size());
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getEpics().size());
    }

    @Test
    void getSubtasks() {
        Assertions.assertEquals(0, manager.getSubtasks().size());

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(new Subtask("Subtask1", "DescSubtask1"), 1);
        manager.addSubtask(new Subtask("Subtask1", "DescSubtask1"), 1);

        Assertions.assertEquals(2, manager.getSubtasks().size());
    }

    @Test
    void getEpicSubtasks() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Subtask1", "DescSubtask1"), 1);
        manager.addSubtask(new Subtask("Subtask1", "DescSubtask1"), 1);

        Assertions.assertEquals(2, manager.getEpicSubtasks(epic1).size());
    }

    @Test
    void getTask() {
        Task actual_task = new Task("","");
        manager.addTask(actual_task);
        int id = actual_task.getId();
        Assertions.assertNotNull(manager.getTask(id));
        Assertions.assertEquals(actual_task, manager.getTask(id));
    }

    @Test
    void getEpic() {
        Epic expected_task = new Epic("","");
        manager.addEpic(expected_task);
        int id = expected_task.getId();
        Assertions.assertNotNull(manager.getEpic(id));
        Assertions.assertEquals(expected_task, manager.getEpic(id));
    }

    @Test
    void getSubtask() {
        manager.addEpic(epic1);
        Subtask expected_task = new Subtask("","");
        manager.addSubtask(expected_task,epic1.getId());
        int id = expected_task.getId();
        Assertions.assertNotNull(manager.getSubtask(id));
        Assertions.assertEquals(expected_task,manager.getSubtask(id));
    }

    @Test
    void deleteTask() {
        manager.addTask(task2);
        int id = manager.addTask(task1);
        Assertions.assertEquals(2, manager.getTasks().size());
        manager.deleteTask(id);
        Assertions.assertEquals(1, manager.getTasks().size());
    }

    @Test
    void deleteEpic() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Subtask subtask = new Subtask("1", "2");
        manager.addSubtask(subtask, epic2.getId());
        Assertions.assertEquals(1, manager.getEpicSubtasks(epic2).size());
        manager.deleteEpic(epic2.getId());
        ArrayList<Epic> arrl = new ArrayList<>();
        arrl.add(epic1);
        Assertions.assertEquals(arrl,manager.getEpics());
    }

    @Test
    void deleteSubtask() {
        int epic = manager.addEpic(epic1);
        int subtask = manager.addSubtask(new Subtask("Subtask1", "for subtask1"), epic);

        manager.deleteSubtask(subtask);

        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, manager.getEpicSubtasks(epic1).size());
    }

    @Test
    void updateTask() {
        class TaskDateComparator implements Comparator<Task> {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getStartTime().compareTo(task2.getStartTime());
            }
        }
        Set<Task> expectedSet = new TreeSet<>(new TaskDateComparator());
        manager.addTask(task1);
        task1.setNote("2");
        task1.setTitle("2");
        task1.setStatus(Status.IN_PROGRESS);
        task1.setStartTime("12:00 01.01.2024");
        task1.setEndTime("14:30 12.05.2024");
        manager.updateTask(task1);
        expectedSet.add(task1);
        Assertions.assertEquals(task1, manager.getTask(task1.getId()));
        Assertions.assertEquals(expectedSet, manager.getPrioritisedTasks());
    }

    @Test
    void updateEpic() {
        class TaskDateComparator implements Comparator<Task> {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getStartTime().compareTo(task2.getStartTime());
            }
        }
        Epic epic = new Epic("title","note");
        manager.addEpic(epic);
        Set <Task> expectedSet = new TreeSet<>(new TaskDateComparator());
        Subtask task = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                ,"12:00 01.01.1111","13:00 01.01.1111");
        manager.addSubtask(task, epic.getId());
        Assertions.assertEquals(Status.NEW, epic.getStatus());
        Assertions.assertEquals(task.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(task.getEndTime(), epic.getEndTime());
        task.setNote("2");
        task.setTitle("2");
        task.setStatus(Status.IN_PROGRESS);
        task.setStartTime("12:00 01.01.2024");
        task.setEndTime("14:30 12.05.2024");
        manager.updateSubtask(task);
        expectedSet.add(task);
        Subtask task2 = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                ,"12:00 01.01.1111","13:00 01.01.1111");
        manager.addSubtask(task2, epic.getId());
        expectedSet.add(task2);
        Assertions.assertEquals(task, manager.getSubtask(task.getId()));
        Assertions.assertEquals(expectedSet, manager.getPrioritisedTasks());
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
        Assertions.assertEquals(task2.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(task.getEndTime(), epic.getEndTime());
        task.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        manager.updateSubtask(task);
        manager.updateSubtask(task2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void updateSubtask() {
        class TaskDateComparator implements Comparator<Task> {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getStartTime().compareTo(task2.getStartTime());
            }
        }
        Epic epic = new Epic("title","note");
        manager.addEpic(epic);
        Set <Task> expectedSet = new TreeSet<>(new TaskDateComparator());
        Subtask task = new Subtask(2, "1", "1", Status.NEW, epic.getId()
                , "12:00 01.01.1111","13:00 01.01.1111");
        manager.addSubtask(task, epic.getId());
        task.setNote("2");
        task.setTitle("2");
        task.setStatus(Status.IN_PROGRESS);
        task.setStartTime("12:00 01.01.2024");
        task.setEndTime("14:30 12.05.2024");
        manager.updateSubtask(task);
        expectedSet.add(task);
        Assertions.assertEquals(task, manager.getSubtask(task.getId()));
        Assertions.assertEquals(expectedSet, manager.getPrioritisedTasks());
    }

    @Test
    void getHistory() {
        //Assertions.assertEquals(0, manager.getHistory().size());
        int expectation = 2;

        int task2Created = manager.addTask(task2);
        int task1Created = manager.addTask(task1);
        manager.getTask(task1Created);
        manager.getTask(task2Created);

        Assertions.assertEquals(expectation, manager.getHistory().size());

        manager.getTask(task1Created);

        Assertions.assertEquals(expectation, manager.getHistory().size());
    }
}
