package ru.yandex.javacource.lagutov.schedule.manager;

import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    ArrayList<Task> getHistory();
    void remove(Task task);
    //так как ArrayList будет удалять 1 вхождение элемента,
    // по смыслу задания лучше как будто использовать очередь как структуру хранения вместо списка
}
