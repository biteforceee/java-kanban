package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;

    private String title;

    private String note;

    private Status status;

    private final TaskType type = TaskType.TASK;

    private LocalDateTime startTime = null;

    private Duration duration = null;

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public Task(String name, String note) {
        this.title = name;
        this.note = note;
        this.id = hashCode();
        this.status = Status.NEW;
    }

    public Task(int id, String title, String note, Status status) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.status = status;
    }

    public Task(int id, String title, String note, Status status, String startTime, String endTime) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, dateFormat);
        this.duration = Duration.between(this.startTime, LocalDateTime.parse(endTime, dateFormat));
    }

    public Task(int id, String title, String note, Status status, String startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, dateFormat);
        this.duration = duration;
    }

    public Task(String title, String note, Status status, String startTime, String endTime) {
        this.id = hashCode();
        this.title = title;
        this.note = note;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, dateFormat);
        this.duration = Duration.between(this.startTime, LocalDateTime.parse(endTime, dateFormat));
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if (title != null) {
            hash = hash + title.hashCode();
        }
        hash = hash * 17;
        if (note != null) {
            hash = hash + note.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDuration() {
        if (duration == null) return 0L;
        return duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, dateFormat);
        if (duration != null) {
            this.duration = Duration.between(this.startTime, getEndTime());
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + getDuration() +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }


}
