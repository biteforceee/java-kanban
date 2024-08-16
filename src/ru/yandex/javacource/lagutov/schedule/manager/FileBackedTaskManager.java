package ru.yandex.javacource.lagutov.schedule.manager;


import ru.yandex.javacource.lagutov.schedule.task.*;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    public FileBackedTaskManager(String file) throws IOException {
        super();
        try {
            filePath = Paths.get(file);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        }
        catch (ManagerSaveException e) {
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
        try (Writer fileWriter = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
            fileWriter.write("ID;TYPE;TITLE;STATUS;NOTE;EPIC" + '\n');
            for (Task task : super.getTasks()) {
                fileWriter.write(toString(task) + '\n');
            }
            for (Epic epic : super.getEpics()) {
                fileWriter.write(toString(epic) + '\n');
                for (Subtask task : super.getSubtasksInEpic(epic)) {
                    fileWriter.write(toString(task) + '\n');
                }
            }
//            fileWriter.write('\n' + "History" + '\n');
//            for (Task task : getHistory()) {
//                fileWriter.write(toString(task) + '\n');
//            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи в файл: " + e.getMessage());
        }
    }

    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line = fileReader.readLine();
            while (fileReader.ready()) {
                //String line = Files.readString(file.toPath());
                line = fileReader.readLine();
                Task task = fromString(line);
                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    addSubtask(subtask, subtask.getEpicID());
                } else {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении из файла: " + e.getMessage());
        }
    }

    public Task fromString(String value) {
        if (value.isBlank()) {
            return null;
        }
        String[] param = value.split(";");
        switch (param[1]) {
            case "Epic":
                Epic epic = new Epic(param[2], param[4]);
                epic.setId(Integer.parseInt(param[0]));
                epic.setStatus(Status.valueOf(param[3].toUpperCase()));
                return epic;
            case "Subtask":
                Subtask subtask = new Subtask(param[2], param[4]);
                subtask.setId(Integer.parseInt(param[0]));
                subtask.setEpicID(Integer.parseInt(param[5]));
                subtask.setStatus(Status.valueOf(param[3].toUpperCase()));
                return subtask;
            case "Task":
                Task task = new Task(param[2], param[4]);
                task.setId(Integer.parseInt(param[0]));
                task.setStatus(Status.valueOf(param[3].toUpperCase()));
                return task;
        }
        return null;
    }

    private String toString(Task task) {
        String[] toJoin = { Integer.toString(task.getId()), task.getType().toString(), task.getTitle(),
                task.getStatus().toString(), task.getNote(), "" };
        if (task instanceof Subtask) {
            toJoin[5] = (Integer.toString(((Subtask) task).getEpicID()));
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

// get методы добавил потомучто не знаю должна тут быть история менеджера или нет
    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask sub = super.getSubtask(id);
        save();
        return sub;
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
