import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public void addTask(Task task) {
        if(tasks.containsValue(task))
            System.out.println("Такая задача уже есть!");
        else tasks.put(task.getId(), task);
    }
    public void addEpic(Epic epic) {
        if (epics.containsValue(epic))
            System.out.println("Такой эпик уже существует!");
        else {
            updateEpicStatus(epic);
            epics.put(epic.getId(), epic);
        }
    }
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicID());
        if(!epic.getSubtasks().contains(subtask)){
            epic.addSubtask(subtask);
            subtasks.put(subtask.getId(), subtask);
        }
    }
    public Task getTaskByID(int id) {
        return tasks.get(id);
    }
    public Epic getEpicByID(int id) {
        return epics.get(id);
    }
    public Subtask getSubtaskByID(int id) {
        return subtasks.get(id);
    }
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }
    public void deleteTasks() {
        tasks.clear();
    }
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubtasks();
            epic.setStatus(Status.NEW);
        }
    }
    public void deleteTaskByID(int id) {
        tasks.remove(id);
    }
    public void deleteTask(Task task) {
        if(tasks.containsValue(task)){
            tasks.remove(task.getId());
        }
    }
    public void deleteEpicByID(int id) {
        epics.remove(id);
    }
    public void deleteEpic(Epic epic) {
        if(epics.containsValue(epic)){
            epics.remove(epic.getId());
        }
    }
    public void deleteSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        int epicID = subtask.getEpicID();
        if(subtasks.containsKey(id)){subtasks.remove(id);}
        Epic epic = epics.get(epicID);
        epic.getSubtasks().remove(subtask);
        updateEpicStatus(epic);
    }
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача не найдена!");
        }
        else {
            tasks.replace(task.getId(), task);
        }
    }
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Эпика не существует!");
        }
        else {
            Epic oldEpic = epics.get(epic.getId());
            ArrayList<Subtask> oldSubtasks= oldEpic.getSubtasks();
            for (Subtask subtask : oldSubtasks) {
                subtasks.remove(subtask.getId());
            }
            epics.replace(epic.getId(), epic);
            ArrayList<Subtask> newEpicSubtaskList = epic.getSubtasks();
            for (Subtask subtask : newEpicSubtaskList) {
                subtasks.put(subtask.getId(), subtask);
            }
            updateEpicStatus(epic);
        }
    }
    public void updateSubtask(Subtask subtask) {
        int epicID = subtask.getEpicID();
        Subtask oldSubtask = subtasks.get(subtask.getId());
        subtasks.replace(subtask.getId(), subtask);
        Epic epic = epics.get(epicID);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(oldSubtask);
        epicSubtasks.add(subtask);
        epic.setSubtasks(epicSubtasks);
        updateEpicStatus(epic);
    }
    public void updateEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) { epic.setStatus(Status.NEW);}
        else {
            int countDone=0;
            for(Subtask sub:epic.getSubtasks()){
                if(sub.getStatus().equals(Status.DONE)) countDone++;
                else if (sub.getStatus().equals(Status.IN_PROGRESS)){
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
            }
            if (countDone == 0) {
                epic.setStatus(Status.NEW);
            } else if (countDone == epic.getSubtasks().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}