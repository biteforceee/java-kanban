package ru.yandex.javacource.lagutov.schedule.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;


class EpicTest {
    Epic epic = new Epic("title","note");

    @org.junit.jupiter.api.Test
    void addSubtaskId() {
        int lid =1;//left
        epic.addSubtaskId(1);
        int rid = epic.getSubtasksIds().getFirst();//right
        Assertions.assertEquals(lid,rid);
    }

    @org.junit.jupiter.api.Test
    void removeSubtaskIds() {
        epic.addSubtaskId(1);
        epic.removeSubtaskIds(1);
        ArrayList<Subtask> arrl=new ArrayList<>();
        Assertions.assertEquals(arrl,epic.getSubtasksIds());
    }

    @org.junit.jupiter.api.Test
    void deleteSubtasks() {
        epic.addSubtaskId(1);
        epic.deleteSubtasks();
        ArrayList<Subtask> arrl=new ArrayList<>();
        Assertions.assertNull(epic.getSubtasksIds());
    }
}