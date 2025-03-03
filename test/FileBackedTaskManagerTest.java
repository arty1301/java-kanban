import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import manager.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertThrows;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @Override
    protected FileBackedTaskManager getTaskManager() {
        try {
            file = File.createTempFile("test", ".csv");
            return new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл", e);
        }
    }

    @Test
    void testSaveAndLoadEmptyFile()  {
        taskManager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertTrue(loadedManager.getAllTasks().isEmpty());
        Assertions.assertTrue(loadedManager.getAllEpics().isEmpty());
        Assertions.assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testFileSaveException() {
        File invalidFile = new File("invalid/path/to/file.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(invalidFile);

        assertThrows(RuntimeException.class, manager::save,
                "Сохранение в неверный файл должно вызывать исключение");
    }
}