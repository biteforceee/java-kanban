package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.time.Duration;

public class Subtask extends Task {
    private int epicId;

    //private final TaskType stype = TaskType.SUBTASK;

    public Subtask(String tasktitle, String subnote) {
        super(tasktitle, subnote);
        type = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String note, Status status, int epicId) {
        super(id, title, note, status);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public Subtask(String tasktitle, String subnote, Epic epic) {
        super(tasktitle, subnote);
        setEpicID(epic.getId());
        type = TaskType.SUBTASK;
    }

    public Subtask(String tasktitle, String subnote, int epic) {
        super(tasktitle, subnote);
        setEpicID(epic);
        type = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String note, Status status, int epicId, String startTime, Duration duration) {
        super(id,title,note,status,startTime,duration);
        setEpicID(epicId);
        type = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String note, Status status, int epicId, String startTime, String endtime) {
        super(id,title,note,status,startTime,endtime);
        setEpicID(epicId);
        type = TaskType.SUBTASK;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
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
                ", type=" + getType() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
