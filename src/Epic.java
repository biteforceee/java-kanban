import java.util.ArrayList;
public class Epic extends Task{
    ArrayList<Subtask> subNotes=new ArrayList<>();
    public Epic(String title, String note) {
        super(title, note);
    }
    public void addSubtask(Subtask task){
        if(!subNotes.contains(task)) {
            subNotes.add(task);
            task.setEpicID(this.getId());
        }
    }
    public void deleteSubtasks(){
        subNotes.clear();
    }
    public ArrayList<Subtask> getSubtasks() {
        return subNotes;
    }
    public Subtask getSubtaskById(int id){
        for(Subtask note: subNotes){
            if(note.getId()==id)return note;
        }
        return null;
    }
    public void setSubtasks(ArrayList<Subtask> subtasks) {
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
