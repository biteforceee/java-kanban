package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.manager.FileBackedTaskManager;
import java.io.File;

import java.io.IOException;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    void setUp() throws IOException {
        File file1 = File.createTempFile("test", "csv");
        super.manager = new FileBackedTaskManager(file1.toString());
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

    @Test
    void loadFromFile() throws IOException {
        int taskExpected = 2;
        int epicExpected = 2;
        int subtaskExpected = 1;

        String fileToLoad = "resources/task.csv";

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(new File(fileToLoad));

        Assertions.assertEquals(taskExpected, manager.getTasks().size());
        Assertions.assertEquals(epicExpected, manager.getEpics().size());
        Assertions.assertEquals(subtaskExpected, manager.getSubtasks().size());
    }
}