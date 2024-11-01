package tests;

import org.junit.jupiter.api.Test;
import task.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testAddSubtaskToItself() {
        Epic epic = new Epic();
        epic.setId(1);

        assertFalse(epic.getSubtaskIds().contains(epic.getId()), "Epic нельзя добавить в самого себя в виде подзадачи");
    }
}
