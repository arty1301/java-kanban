package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void addTask(Task task);

    void updateTask(Task task);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    List<Subtask> getSubtasksByEpic(int epicId);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();
}
