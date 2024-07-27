package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.HistoryManager;
import ru.yandex.javacource.lagutov.schedule.manager.Managers;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;


class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();
    @Test
    void add() {
        Task task = new Task("name1","note");
        historyManager.add(task);
        historyManager.add(task);
        Assertions.assertEquals(1,historyManager.getHistory().size());
    }

    @Test
    void remove() {
        Task task = new Task("name","note");
        historyManager.add(task);
        historyManager.remove(task.getId());
        Assertions.assertEquals(0,historyManager.getHistory().size());
    }

    @Test
    void getHistory() {
        List<Task> list = new ArrayList<>();
        Task task1 = new Task("name1","note");
        Task task2 = new Task("name2","note");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        list.add(task2);
        list.add(task1);
        Assertions.assertEquals(list,historyManager.getHistory());
    }
}