package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String title, String note) {
        super(title, note);
        type = TaskType.EPIC;
    }

    public Epic(int id, String title, String note, Status status) {
        super(id, title, note, status);
        type = TaskType.EPIC;
    }

    public Epic(int id, String title, String note, Status status, String startTime, Duration duration) {
        super(id,title,note,status,startTime,duration);
        this.endTime = super.getEndTime();
        type = TaskType.EPIC;
    }

    public Epic(int id, String title, String note, Status status, String startTime, String endTime) {
        super(id,title,note,status,startTime,endTime);
        type = TaskType.EPIC;
    }

    public TaskType getType() {
        return TaskType.EPIC;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasks) {
        subtaskIds = subtasks;
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalDateTime.parse(endTime, dateFormat);
        if (getStartTime() != null) {
            setDuration(Duration.between(getStartTime(), this.endTime));
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", note='" + getNote() + '\'' +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
            '}';
    }
}
