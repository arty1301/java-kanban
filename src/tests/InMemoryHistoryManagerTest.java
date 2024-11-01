package tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

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
    void testHistoryLimit() {
        for (int i = 0; i < 12; i++) {
            Task task = new Task();
            task.setId(i);
            task.setTitle("Задача" + i);
            historyManager.add(task);
        }

        assertEquals(10, historyManager.getHistory().size(),
                "История просмотров должна содержать последние 10 просмотренных задач");
    }
}
