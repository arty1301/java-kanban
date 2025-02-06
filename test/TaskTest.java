import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

class TaskTest {
    @Test
    void testEquals() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Задача 1");
        task1.setStatus(TaskStatus.NEW);

        Task task2 = new Task();
        task2.setId(1);
        task2.setTitle("Задача 1");
        task2.setStatus(TaskStatus.NEW);

        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равными");
    }

    @Test
    void testNotEquals() {
        Task task1 = new Task();
        task1.setId(1);

        Task task2 = new Task();
        task2.setId(2);

        assertNotEquals(task1, task2, "Задачи с разными ID должны быть разными");
    }
}
