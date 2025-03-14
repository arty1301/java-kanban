import adapter.GsonCreate;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import task.Task;
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

class HttpTaskManagerTasksTest extends HttpTaskManagerTestBase {
    private final Gson gson = GsonCreate.createGson();

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task();
        task.setTitle("Задача");
        task.setDescription("Описание");
        task.setStatus(TaskStatus.NEW);
        task.setDuration(Duration.ofMinutes(30));
        task.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код ответа");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task();
        task.setTitle("Задача");
        task.setDescription("Описание");
        task.setStatus(TaskStatus.NEW);
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(retrievedTask, "Задача не найдена");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task();
        task.setTitle("Задача");
        task.setDescription("Описание");
        task.setStatus(TaskStatus.NEW);
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNull(manager.getTask(task.getId()), "Задача не удалена");
    }
}