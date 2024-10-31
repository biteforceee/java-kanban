package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.time.Duration;

public class Subtask extends Task {
    private int epicId;

    private final TaskType type = TaskType.SUBTASK;

    public Subtask(String tasktitle, String subnote) {
        super(tasktitle, subnote);
    }

    public Subtask(int id, String title, String note, Status status, int epicId) {
        super(id, title, note, status);
        this.epicId = epicId;
    }

    public Subtask(String tasktitle, String subnote, Epic epic) {
        super(tasktitle, subnote);
        setEpicID(epic.getId());
    }

    public Subtask(String tasktitle, String subnote, int epic) {
        super(tasktitle, subnote);
        setEpicID(epic);
    }

    public Subtask(int id, String title, String note, Status status, int epicId, String startTime, Duration duration) {
        super(id,title,note,status,startTime,duration);
        setEpicID(epicId);
    }

    public Subtask(int id, String title, String note, Status status, int epicId, String startTime, String endtime) {
        super(id,title,note,status,startTime,endtime);
        setEpicID(epicId);
    }

    public TaskType getType() {
        return type;
    }

    public void setEpicID(int epicID) {
        this.epicId = epicID;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", epicID='" + epicId + '\'' +
                ", note='" + getNote() + '\'' +
                ", status=" + getStatus() +
                ", type=" + type +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
