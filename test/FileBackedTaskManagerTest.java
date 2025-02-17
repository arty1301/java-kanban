import manager.FileBackedTaskManager;
import org.junit.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
    @Test
    public void testSaveAndLoadEmptyFile() throws IOException {
        File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    public void testSaveMultipleTasks() throws IOException {
        File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task = new Task();
        task.setTitle("Задача 1");
        manager.addTask(task);

        Epic epic = new Epic();
        epic.setTitle("Эпик 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Подзадача 1");
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);

        assertEquals(1, manager.getAllTasks().size(), "Должна быть одна задача");
        assertEquals(1, manager.getAllEpics().size(), "Должен быть один эпик");
        assertEquals(1, manager.getAllSubtasks().size(), "Должна быть одна подзадача");
    }

    @Test
    public void testLoadMultipleTasks() throws IOException {
        File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task = new Task();
        task.setTitle("Задача 1");
        manager.addTask(task);

        Epic epic = new Epic();
        epic.setTitle("Эпик 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Подзадача 1");
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, loadedManager.getAllTasks().size(), "Должна быть одна задача");
        assertEquals(1, loadedManager.getAllEpics().size(), "Должен быть один эпик");
        assertEquals(1, loadedManager.getAllSubtasks().size(), "Должна быть одна подзадача");

        Task loadedTask = loadedManager.getTask(task.getId());
        assertEquals(task.getTitle(), loadedTask.getTitle(), "Название задачи должно совпадать");

        Epic loadedEpic = loadedManager.getEpic(epic.getId());
        assertEquals(epic.getTitle(), loadedEpic.getTitle(), "Название эпика должно совпадать.");

        Subtask loadedSubtask = loadedManager.getSubtask(subtask.getId());
        assertEquals(subtask.getTitle(), loadedSubtask.getTitle(), "Название подзадачи должно совпадать.");
        assertEquals(subtask.getEpicId(), loadedSubtask.getEpicId(), "ID эпика подзадачи должно совпадать.");
    }

}
