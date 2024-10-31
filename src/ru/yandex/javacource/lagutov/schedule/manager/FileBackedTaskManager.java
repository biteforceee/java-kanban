package ru.yandex.javacource.lagutov.schedule.manager;


import ru.yandex.javacource.lagutov.schedule.manager.exeptions.ManagerSaveException;
import ru.yandex.javacource.lagutov.schedule.task.*;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    private static final String fileHeader = "id;type;title;status;note;epic;startTime;endTime;duration;";

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public FileBackedTaskManager(String file) throws IOException {
        super();
        try {
            filePath = Paths.get(file);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка получения файла: " + e.getMessage());
        }
    }

    public FileBackedTaskManager(File file) throws IOException {
        super();
        try {
            filePath = Paths.get(file.toString());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка получения файла: " + e.getMessage());
        }
    }

    public void save() {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), StandardCharsets.UTF_8))) {
            writer.write(fileHeader);
            writer.newLine();
            for (Task task : super.getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : super.getEpics()) {
                writer.write(toString(epic));
                writer.newLine();
                for (Subtask task : super.getSubtasksInEpic(epic)) {
                    writer.write(toString(task));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Can't write in file: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    break;
                }
                final Task task = taskFromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
            }
            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TaskType.TASK:
                tasks.put(id, task);
                break;
            case TaskType.SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case TaskType.EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    public static Task taskFromString(String value) {
        if (value.isBlank()) {
            return null;
        }
        String[] param = value.split(";");
        if (param.length > 6) {//!param[8].isEmpty() && !param[6].isEmpty() &&
            return switch (param[1]) {
                case "EPIC" -> new Epic(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()), param[6], param[7]);
                case "SUBTASK" -> new Subtask(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()), Integer.parseInt(param[5]), param[6], param[7]);
                case "TASK" -> new Task(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()), param[6], param[7]);
                default -> null;
            };
        } else {
            return switch (param[1]) {
                case "EPIC" -> new Epic(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()));
                case "SUBTASK" -> new Subtask(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()), Integer.parseInt(param[5]));
                case "TASK" -> new Task(Integer.parseInt(param[0]), param[2], param[4],
                        Status.valueOf(param[3].toUpperCase()));
                default -> null;
            };
        }
    }

    private String toString(Task task) {
        String[] toJoin = {Integer.toString(task.getId()), task.getType().toString(), task.getTitle(),
                task.getStatus().toString(), task.getNote(), ""};
        if (task.getStartTime() != null && task.getEndTime() != null) {
            toJoin = new String[]{Integer.toString(task.getId()), task.getType().toString(), task.getTitle(),
                    task.getStatus().toString(), task.getNote(), "", task.getStartTime().format(dateFormat),
                    task.getEndTime().format(dateFormat), Long.toString(task.getDuration())};
        }
        if (task instanceof Subtask) {
            toJoin[5] = (Integer.toString(((Subtask) task).getEpicId()));
        }
        return String.join(";", toJoin);
    }

    @Override
    public Integer addEpic(Epic task) {
        int id = super.addEpic(task);
        save();
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask, int epicId) {
        int id = super.addSubtask(subtask, epicId);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}
