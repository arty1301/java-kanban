import adapter.GsonCreate;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerSubtasksTest extends HttpTaskManagerTestBase {
    private final Gson gson = GsonCreate.createGson();

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic();
        epic.setTitle("Эпик");
        epic.setDescription("Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Подзадача");
        subtask.setDescription("Описание");
        subtask.setStatus(TaskStatus.NEW);
        subtask.setEpicId(epic.getId());
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());

        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код ответа");

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Неверное количество подзадач");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic();
        epic.setTitle("Эпик");
        manager.addEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Подзадача");
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        Subtask retrievedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(retrievedSubtask, "Подзадача не найдена");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic();
        epic.setTitle("Эпик");
        manager.addEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Подзадача");
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNull(manager.getSubtask(subtask.getId()), "Подзадача не удалена");
    }
}