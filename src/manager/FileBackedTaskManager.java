package manager;

import exception.FileManagerFileInitializationException;
import exception.FileManagerSaveException;
import task.*;
import util.FormatUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    public void save() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,type,name,status,description,epic");

            for (Task task : getAllTasks()) {
                lines.add(FormatUtil.taskToString(task));
            }
            for (Epic epic : getAllEpics()) {
                lines.add(FormatUtil.taskToString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                lines.add(FormatUtil.taskToString(subtask));
            }

            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            String errorMessage = "Ошибка сохранения файла" + e.getMessage();
            System.out.println(errorMessage);
            throw new FileManagerSaveException(errorMessage);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = FormatUtil.taskFromString(lines[i]);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            String errorMessage = "Ошибка загрузки файла" + e.getMessage();
            System.out.println(errorMessage);
            throw new FileManagerFileInitializationException(errorMessage);
        }
        return manager;
    }
}