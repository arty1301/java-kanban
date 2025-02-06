import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
