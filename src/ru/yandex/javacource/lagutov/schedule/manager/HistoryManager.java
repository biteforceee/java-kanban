package ru.yandex.javacource.lagutov.schedule.manager;

import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    //void remove(Task task);
    //так как ArrayList будет удалять 1 вхождение элемента,
    //по смыслу задания лучше как будто использовать очередь как структуру хранения вместо списка
    void remove(int id);
}
