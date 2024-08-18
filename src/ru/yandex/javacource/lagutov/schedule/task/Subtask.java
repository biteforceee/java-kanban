package ru.yandex.javacource.lagutov.schedule.task;

import ru.yandex.javacource.lagutov.schedule.manager.TaskType;

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
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getTitle() + '\'' +
                ", epicID='" + epicId + '\'' +
                ", description='" + getNote() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
