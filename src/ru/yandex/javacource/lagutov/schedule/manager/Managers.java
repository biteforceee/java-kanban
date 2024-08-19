package ru.yandex.javacource.lagutov.schedule.manager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
