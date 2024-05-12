import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subNotes=new ArrayList<>();

    public Epic(String title, String note) {
        super(title, note);
    }

    public void addSubtask(Subtask task){
        if(!subNotes.contains(task)) {
            subNotes.add(task.getId());
            task.setEpicID(this.getId());
        }
    }
    public void removeSubtask(int id){
        subNotes.remove(id);
    }
    public void deleteSubtasks(){
        subNotes.clear();
    }

    public ArrayList<Integer> getSubtasks() {
        return subNotes;
    }

    public Integer getSubtask(int id){
        for(Integer note: subNotes){
            if(note==id)
                return note;
        }
        return null;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        subNotes = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", note='" + getNote() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
