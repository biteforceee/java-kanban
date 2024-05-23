package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_COUNT=10;
    private ArrayList<Task> qHistory = new ArrayList<>();
    @Override
    public void add(Task task) {
        if(qHistory.size()>MAX_COUNT){
            qHistory.remove(0);
        }
        qHistory.add(task);
    }
    @Override
    public ArrayList<Task> getHistory() {
        return qHistory;
    }
    @Override
    public void remove(Task task){
        qHistory.remove(task);
    }
}
