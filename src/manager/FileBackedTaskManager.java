package manager;

import exception.FileManagerFileInitializationException;
import exception.FileManagerSaveException;
import task.*;

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
                lines.add(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                lines.add(toString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                lines.add(toString(subtask));
            }

            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            String errorMessage = "Ошибка сохранения файла" + e.getMessage();
            System.out.println(errorMessage);
            throw new FileManagerSaveException(errorMessage);
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                TaskType.TASK,
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                "");
    }

    private String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s",
                epic.getId(),
                TaskType.EPIC,
                epic.getTitle(),
                epic.getStatus(),
                epic.getDescription(),
                "");
    }

    private String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d",
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getTitle(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getEpicId());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = fromString(lines[i]);
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

    private static Task fromString(String lineValue) {
        if (lineValue == null || lineValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустая строка не может быть преобразована в задачу");
        }
        String[] element = lineValue.split(",");
        try {
            int id = Integer.parseInt(element[0].trim());
            TaskType type = TaskType.valueOf(element[1].trim());
            String title = element[2].trim();
            TaskStatus status = TaskStatus.valueOf(element[3].trim());
            String description = element[4].trim();

            switch (type) {
                case TASK:
                    Task task = new Task();
                    task.setId(id);
                    task.setTitle(title);
                    task.setStatus(status);
                    task.setDescription(description);
                    return task;
                case EPIC:
                    Epic epic = new Epic();
                    epic.setId(id);
                    epic.setTitle(title);
                    epic.setStatus(status);
                    epic.setDescription(description);
                    return epic;
                case SUBTASK:
                    Subtask subtask = new Subtask();
                    subtask.setId(id);
                    subtask.setTitle(title);
                    subtask.setStatus(status);
                    subtask.setDescription(description);
                    subtask.setEpicId(Integer.parseInt(element[5].trim()));
                    return subtask;
                default:
                    throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = "Некорректные данные в строке: " + e.getMessage();
            System.out.println(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
