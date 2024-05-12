
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task a=new Task("title1","note1");
        Task b=new Task("title2","note2");
        taskManager.addTask(a);
        a.setStatus(Status.DONE);
        taskManager.addTask(b);
        System.out.println(taskManager.getTasks());
        taskManager.deleteTaskByID(-1858796295);
        System.out.println(taskManager.getTasks());
        taskManager.deleteTasks();
        System.out.println(taskManager.getTasks());

        Epic epic1 =new Epic("title1","note1");
        Subtask aa =new Subtask("subTitle1","subNote1");
        epic1.addSubtask(aa);
        Subtask bb =new Subtask("subTitle2","subNote2");
        epic1.addSubtask(bb);
        taskManager.addEpic(epic1);
        aa.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic1);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasks());
        }
        Epic epic2 =new Epic("title2","note2");
        Subtask cc = new Subtask("subTitle3","subNote3");
        epic2.addSubtask(cc);
        taskManager.addEpic(epic2);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasks());
        }
        //taskManager.deleteEpicByID(-1858796277);
        taskManager.deleteEpic(epic2);
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasks());
        }
        taskManager.deleteEpics();
        System.out.println("//////////////////////");
        for(Epic epic :taskManager.getEpics()){
            System.out.println(epic.toString());
            System.out.println(epic.getSubtasks());
        }
    }
}
