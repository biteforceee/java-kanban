package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_COUNT=10;
    private final List<Task> qHistory = new ArrayList<>();


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if(qHistory.size() >= MAX_COUNT){
            qHistory.remove(0);
        }
        qHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return qHistory;
    }

    @Override
    public void remove(Task task){
        qHistory.remove(task);
    }
}
