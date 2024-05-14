package ru.yandex.javacource.lagutov.schedule.task;

public class Task {
    private int id;

    private String title;

    private String note;

    private Status status;

    public Task(String name, String note) {
        this.title = name;
        this.note = note;
        this.id = hashCode();
        this.status = Status.NEW;
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
    @Override
    public String toString() {
        return "ru.yandex.javacource.lagutov.schedule.task.Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                '}';
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
}
