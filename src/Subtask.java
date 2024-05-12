public class Subtask extends Task{
    private int epicID=0;
    Subtask(String tasktitle,String subnote) {
        super(tasktitle, subnote);
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getTitle() + '\'' +
                ", epicID='" + epicID +'\'' +
                ", description='" + getNote() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
