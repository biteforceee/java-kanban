package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    private final TaskType type = TaskType.EPIC;

    public Epic(String title, String note) {
        super(title, note);
    }

    public Epic(int id, String title, String note, Status status) {
        super(id, title, note, status);
    }

    public Epic(int id, String title, String note, Status status, String startTime, Duration duration) {
        super(id,title,note,status,startTime,duration);
    }

    public Epic(int id, String title, String note, Status status, String startTime, String endTime) {
        super(id,title,note,status,startTime,endTime);
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
                ", type=" + type +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
