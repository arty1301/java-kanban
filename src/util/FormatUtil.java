package util;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class FormatUtil {
    public static String taskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s",
                task.getId(),
                TaskType.TASK,
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                "",
                task.getDuration().toMinutes(),
                task.getStartTime());
    }


    public static String taskToString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s",
                epic.getId(),
                TaskType.EPIC,
                epic.getTitle(),
                epic.getStatus(),
                epic.getDescription(),
                "",
                epic.getDuration().toMinutes(),
                epic.getStartTime());
    }

    public static String taskToString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d,%d,%s",
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getTitle(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getEpicId(),
                subtask.getDuration().toMinutes(),
                subtask.getStartTime());
    }

    public static Task taskFromString(String lineValue) {
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
            Duration duration = Duration.ofMinutes(Long.parseLong(element[6].trim()));
            LocalDateTime startTime = element[7].trim().isEmpty() ? null
                    : LocalDateTime.parse(element[7].trim());

            switch (type) {
                case TASK:
                    Task task = new Task();
                    task.setId(id);
                    task.setTitle(title);
                    task.setStatus(status);
                    task.setDescription(description);
                    task.setDuration(duration);
                    task.setStartTime(startTime);
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
