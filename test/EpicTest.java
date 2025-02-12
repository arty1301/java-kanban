import org.junit.jupiter.api.Test;
import task.Epic;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testAddSubtaskToItself() {
        Epic epic = new Epic();
        epic.setId(1);

        assertFalse(epic.getSubtaskIds().contains(epic.getId()), "Epic нельзя добавить в самого себя в виде подзадачи");
    }

    @Test
    void testEpicStatusWhenNoSubtasks() {
        Epic epic = new Epic();
        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус эпика без подзадач должен быть NEW");
    }
}
