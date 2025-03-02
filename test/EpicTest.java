import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;
import manager.TaskManager;
import manager.Managers;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager taskManager;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefaultTaskManager();
        epic = new Epic();
        taskManager.addEpic(epic);
        subtask1 = new Subtask();
        subtask2 = new Subtask();
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
    }

    @Test
    void testAddSubtaskToItself() {
        epic.setId(1);
        assertFalse(epic.getSubtaskIds().contains(epic.getId()),
                "Epic нельзя добавить в самого себя в виде подзадачи");
    }

    @Test
    void testEpicStatusWhenNoSubtasks() {
        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус эпика без подзадач должен быть NEW");
    }

    @Test
    void testEpicStatusAllNew() {
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус эпика должен быть NEW, если все подзадачи NEW");
    }

    @Test
    void testEpicStatusAllDone() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.DONE, epic.getStatus(),
                "Статус эпика должен быть DONE, если все подзадачи DONE");
    }

    @Test
    void testEpicStatusNewAndDone() {
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус эпика должен быть IN_PROGRESS, если подзадачи NEW и DONE");
    }

    @Test
    void testEpicStatusInProgress() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус эпика должен быть IN_PROGRESS, если все подзадачи IN_PROGRESS");
    }
}