package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    private final TaskType type = TaskType.EPIC;

    public Epic(String title, String note) {
        super(title, note);
    }

    public Epic(int id, String title, String note, Status status) {
        super(id, title, note, status);
    }

    public TaskType getType() {
        return type;
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public void removeSubtaskIds(Integer id) {
        subtaskIds.remove(id);
    }

    public void deleteSubtasks() {
        subtaskIds.clear();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtaskIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasks) {
        subtaskIds = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", note='" + getNote() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
