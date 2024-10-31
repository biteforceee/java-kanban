package ru.yandex.javacource.lagutov.schedule.manager;
import ru.yandex.javacource.lagutov.schedule.task.Epic;
import ru.yandex.javacource.lagutov.schedule.task.Status;
import ru.yandex.javacource.lagutov.schedule.task.Subtask;
import ru.yandex.javacource.lagutov.schedule.task.Task;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    static class TaskDateComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    }

    protected Set<Task> prioritisedTasks = new TreeSet<>(new TaskDateComparator());

    protected int generatorId = 0;

    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public boolean checkTime(Task task) {
        if (!prioritisedTasks.isEmpty()) {
            for (Task t : prioritisedTasks) {
                if (!(task.getEndTime().isBefore(t.getStartTime()) || task.getStartTime().isAfter(t.getEndTime()))
                        && task.getId() != t.getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void validateTime(Task task) throws IllegalStateException {
        boolean check = checkTime(task);
        if (!check) {
            throw new IllegalStateException("задача имеет пересечение");
        }
    }

    private void calcEpicTime(Epic epic) {
        LocalDateTime minSubtaskStartTime;
        LocalDateTime maxSubtaskEndTime;

        List<Subtask> subtasks = getSubtasksInEpic(epic);

        try {
            minSubtaskStartTime = subtasks.stream()
                    .map(Subtask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElseThrow();

            maxSubtaskEndTime = subtasks.stream()
                    .map(Subtask::getEndTime)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            System.out.println("Не установлено время выполнение для подзадачи");
            //throw new NoSuchElementException("123");
            return;
        }

        epic.setStartTime(minSubtaskStartTime.format(dateFormat));
        epic.setEndTime(maxSubtaskEndTime.format(dateFormat));
    }

    @Override
    public int addTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            validateTime(task);
            prioritisedTasks.add(task);
        }
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        if (epic.getStartTime() != null) {
            updateEpicStatus(epic.getId());
            calcEpicTime(epic);
            prioritisedTasks.add(epic);
        }
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask, int epicId) {
        if (subtask.getStartTime() != null) {
            validateTime(subtask);
        }
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
        if (subtask.getStartTime() != null) {
            prioritisedTasks.add(subtask);
            calcEpicTime(epic);
        }
        calcEpicTime(epic);
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

    public Set<Task> getPrioritisedTasks() {
        return prioritisedTasks;
    }

    public List<Subtask> getSubtasksInEpic(Epic epic) {
        return getSubtasks().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTasks() {
        for (Task task : getTasks()) {
            prioritisedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Subtask subtask : getSubtasks()) {
            prioritisedTasks.remove(subtask);
        }
        for (Epic epic : getEpics()) {
            prioritisedTasks.remove(epic);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        if (tasks.get(id).getStartTime() != null && tasks.get(id).getEndTime() != null) {
            prioritisedTasks.remove(tasks.get(id));
        }
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
            if (subtasks.get(subtaskId).getStartTime() != null && subtasks.get(subtaskId).getEndTime() != null) {
                prioritisedTasks.remove(subtasks.get(subtaskId));
            }
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
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskIds(id);
        if (subtask.getStartTime() != null && subtask.getEndTime() != null) {
            prioritisedTasks.remove(subtask);
        }
        calcEpicTime(epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача не найдена!");
        } else {
            if (task.getStartTime() != null) {
                Task oldTask = tasks.get(task.getId());
                prioritisedTasks.remove(oldTask);
                validateTime(task);
                prioritisedTasks.add(task);
            }
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
        if (subtask.getStartTime() != null) {
            validateTime(subtask);
            Subtask oldSubtask = subtasks.get(subtask.getId());
            prioritisedTasks.remove(oldSubtask);
            prioritisedTasks.add(subtask);
            calcEpicTime(epics.get(subtask.getEpicId()));
        }
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
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