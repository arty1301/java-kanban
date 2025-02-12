package task;

public class Subtask extends Task {
    private int epicId;


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Название: '" + getTitle() + "', Описание: '" + getDescription() + "', Статус: " + getStatus();
    }
}
