package ru.yandex.javacource.lagutov.schedule.manager;//package ru.yandex.javacource.твояфамилия.schedule.manager;

import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int generatorId = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();

    private HashMap<Integer, Epic> epics = new HashMap<>();

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int addTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            System.out.println("Такой эпик уже существует!");
            return -1;
        }
        else {
            int id = ++generatorId;
            epic.setId(id);
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
            return id;
        }
    }

    public Integer addSubtask(Subtask subtask, Epic epc) { // или передавать вместо ru.yandex.javacource.lagutov.schedule.task.Epic epc, int epicId, но так понятней
        int epicId = epc.getId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        subtask.setEpicID(epicId);
        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epc.addSubtaskId(subtask.getId());
        updateEpicStatus(epic.getId());
        return id;
    }
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Integer> getEpicSubtasks(Epic epic) {
        return epic.getSubtasksIds();
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }


    public void deleteEpic(Epic epic) {
        if(epics.containsValue(epic)){
            epics.remove(epic.getId());
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicID());
        epic.removeSubtaskIds(id);
        updateEpicStatus(epic.getId());
    }

    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача не найдена!");
        } else {
            tasks.replace(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setTitle(epic.getTitle());
        savedEpic.setNote(epic.getNote());
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicID();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
    }

    public void updateEpicStatus(int epicId) {
        if (epics.get(epicId).getSubtasksIds().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        }
        else {
            int countDone=0;
                for(int subId: epics.get(epicId).getSubtasksIds()){
                    if(subtasks.get(subId).getStatus().equals(Status.DONE)) {
                        countDone++;
                    }
                    else if (subtasks.get(subId).getStatus().equals(Status.IN_PROGRESS)) {
                        epics.get(epicId).setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }
            if (countDone == 0) {
                epics.get(epicId).setStatus(Status.NEW);
            } else if (countDone == epics.get(epicId).getSubtasksIds().size()) {
                epics.get(epicId).setStatus(Status.DONE);
            } else {
                epics.get(epicId).setStatus(Status.IN_PROGRESS);
            }
        }
    }
}