package tests;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddTask() {
        Task task = new Task();
        task.setTitle("Заголовок");
        task.setStatus(TaskStatus.NEW);

        taskManager.addTask(task);
        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача должна быть добавлена");
        assertEquals(task, savedTask, "Сохраненная задача должна соответствовать начальной");
    }

    @Test
    void testAddEpicAndSubtask() {
        Epic epic = new Epic();
        epic.setTitle("Заголовок");

        taskManager.addEpic(epic);
        Subtask subtask = new Subtask();
        subtask.setTitle("Заголовок");
        subtask.setEpicId(epic.getId());

        taskManager.addSubtask(subtask);

        assertTrue(taskManager.getSubtasksByEpic(epic.getId()).contains(subtask),
                "Subtask должен быть связан с Epic");
    }

    @Test
    void testRemoveTask() {
        Task task = new Task();
        task.setTitle("Заголовок");
        taskManager.addTask(task);
        int id = task.getId();

        taskManager.removeTask(id);

        assertNull(taskManager.getTask(id), "Задача должна быть удалена");
    }
}
