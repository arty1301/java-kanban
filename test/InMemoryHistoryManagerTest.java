import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddToHistory() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Заголовок");

        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task),
                "Задача должна быть добавлена в историю просмотров");
    }

    @Test
    void testRemoveFromHistory() {
        Task task = new Task();
        task.setId(1);
        historyManager.add(task);

        historyManager.remove(task.getId());
        assertFalse(historyManager.getHistory().contains(task),
                "Задача должна быть удалена из истории");
    }

    @Test
    void testNoDuplicatesInHistory() {
        Task task = new Task();
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(),
                "История не должна повторяться");
    }

    @Test
    void testHistoryOrder() {
        Task task1 = new Task();
        task1.setId(1);
        Task task2 = new Task();
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(task1, history.get(0), "Первая задача -> task1");
        assertEquals(task2, history.get(1), "Вторая задача -> task2");
    }

    @Test
    void testRemoveEpicWithThreeSubtasksAndCheckHistory() {
        Epic epic = new Epic();
        epic.setId(1);
        epic.setTitle("Эпик 1");

        Subtask subtask1 = new Subtask();
        subtask1.setId(2);
        subtask1.setTitle("Подзадача 1");
        subtask1.setEpicId(epic.getId());
        epic.getSubtaskIds().add(subtask1.getId());

        Subtask subtask2 = new Subtask();
        subtask2.setId(3);
        subtask2.setTitle("Подзадача 2");
        subtask2.setEpicId(epic.getId());
        epic.getSubtaskIds().add(subtask2.getId());

        Subtask subtask3 = new Subtask();
        subtask3.setId(4);
        subtask3.setTitle("Подзадача 3");
        subtask3.setEpicId(epic.getId());
        epic.getSubtaskIds().add(subtask3.getId());

        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask3);

        historyManager.remove(epic.getId());

        List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(epic), "Эпик должен быть удален из истории");
        assertFalse(history.contains(subtask1), "Подзадача 1 должна быть удалена из истории");
        assertFalse(history.contains(subtask2), "Подзадача 2 должна быть удалена из истории");
        assertFalse(history.contains(subtask3), "Подзадача 3 должна быть удалена из истории");
    }
}

