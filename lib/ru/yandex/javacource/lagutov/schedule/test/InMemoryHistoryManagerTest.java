package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.HistoryManager;
import ru.yandex.javacource.lagutov.schedule.manager.InMemoryHistoryManager;
import ru.yandex.javacource.lagutov.schedule.manager.Managers;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager= Managers.getDefaultHistory();
    @Test
    void add() {
        Task task = new Task("name","note");
        historyManager.add(task);
        Assertions.assertEquals(1,historyManager.getHistory().size());
    }

    @Test
    void remove() {
        Task task = new Task("name","note");
        historyManager.add(task);
        historyManager.remove(task);
        Assertions.assertEquals(0,historyManager.getHistory().size());
    }
}