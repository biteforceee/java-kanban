package ru.yandex.javacource.lagutov.schedule.manager;

import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public interface TaskManager {
    List<Task> getHistory();

    int addTask(Task task);

    Integer addEpic(Epic epic);

    Integer addSubtask(Subtask subtask, int epicId);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Integer> getEpicSubtasks(Epic epic);

    void deleteTasks();

    void deleteEpics();

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteEpic(Epic epic);

    void deleteSubtask(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(int epicId);
}
