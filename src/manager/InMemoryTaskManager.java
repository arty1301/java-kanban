package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int nextId = 1;

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            priorityTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    priorityTasks.remove(subtask);
                }
            }
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            priorityTasks.remove(subtask);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void addTask(Task task) {
        if (hasIntersects(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей");
        }
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            priorityTasks.add(task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (hasIntersects(task)) {
                throw new IllegalArgumentException("Задача пересекается по времени с другой задачей");
            }
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                priorityTasks.add(task);
            }
        } else {
            System.out.println("Задача c ID: " + task.getId() + " не найдена. Введите другое значение.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        } else {
            System.out.println("Эпик с ID: " + epic.getId() + " не найден. Введите другое значение.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (hasIntersects(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с задачей");
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtaskIds().add(subtask.getId());
        updateEpicStatus(epic);
        epic.updateEpicTime(getSubtasksByEpic(epic.getId()));
        if (subtask.getStartTime() != null) {
            priorityTasks.add(subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (hasIntersects(subtask)) {
                throw new IllegalArgumentException("подзадача пересакается по времени с задачей");
            }
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epic);
                epic.updateEpicTime(getSubtasksByEpic(epic.getId()));
                if (subtask.getStartTime() != null) {
                    priorityTasks.add(subtask);
                }
            } else {
                System.out.println("Подзадача с ID: " + subtask.getId() +
                        " не обновлена, потому что не имеет связанного эпика");
            }
        } else {
            System.out.println("Подзадача с ID: " + subtask.getId() + " не найдена. Введите другое значение.");
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void removeTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            priorityTasks.remove(task);
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.remove(subtaskId);
                if (subtask != null) {
                    historyManager.remove(subtaskId);
                    priorityTasks.remove(subtask);
                }
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
        }
        historyManager.remove(id);
        priorityTasks.remove(subtask);

    }

    @Override
    public List<Subtask> getSubtasksByEpic(int epicId) {
        return Optional.ofNullable(epics.get(epicId))
                .map(epic -> epic.getSubtaskIds().stream()
                        .map(subtasks::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Эпик не может быть Null");
        }
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean done = true;
        boolean newStatus = true;
        boolean inProgress = false;

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Task subtask = getSubtask(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() != TaskStatus.DONE) {
                    done = false;
                }
                if (subtask.getStatus() != TaskStatus.NEW) {
                    newStatus = false;
                }
                if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                    inProgress = true;
                }
            }
        }

        if (inProgress) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (done) {
            epic.setStatus(TaskStatus.DONE);
        } else if (newStatus) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priorityTasks);
    }

    private boolean isIntersects(Task task1, Task task2) {
        return task1.getEndTime().isAfter(task2.getStartTime()) &&
                task1.getStartTime().isBefore(task2.getEndTime());
    }

    private boolean hasIntersects(Task task) {
        return priorityTasks.stream()
                .anyMatch(task2 -> isIntersects(task, task2));
    }
}
