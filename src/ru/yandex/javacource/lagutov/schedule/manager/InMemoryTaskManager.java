package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.util.*;

    public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generatorId = 0;

    private final Map<Integer, Task> tasks = new HashMap<>();

    private final Map<Integer, Epic> epics = new HashMap<>();

    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int addTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask, int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        subtask.setEpicID(epicId);
        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic.getId());
        return id;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask sub = subtasks.get(id);
        historyManager.add(sub);
        return sub;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Integer> getEpicSubtasks(Epic epic) {
        return epic.getSubtasksIds();
    }

    public ArrayList<Subtask> getSubtasksInEpic(Epic epic) { // или сделать так чтобы epic хранил не ID а Subtask
        ArrayList<Subtask> list = new ArrayList<>();
        for (int id : epic.getSubtasksIds()) {
            list.add(subtasks.get(id));
        }
        return list;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        historyManager.remove(id);
        Epic epic = epics.get(subtask.getEpicID());
        epic.removeSubtaskIds(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача не найдена!");
        } else {
            tasks.replace(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setTitle(epic.getTitle());
        savedEpic.setNote(epic.getNote());
    }

    @Override
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

    @Override
    public void updateEpicStatus(int epicId) {
        if (epics.get(epicId).getSubtasksIds().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        } else {
            int countDone = 0;
                for (int subId: epics.get(epicId).getSubtasksIds()) {
                    if (subtasks.get(subId).getStatus().equals(Status.DONE)) {
                        countDone++;
                    } else if (subtasks.get(subId).getStatus().equals(Status.IN_PROGRESS)) {
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