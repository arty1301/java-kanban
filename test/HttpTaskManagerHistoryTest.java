import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerHistoryTest extends HttpTaskManagerTestBase {

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task();
        task.setTitle("Задача");
        task.setDescription("Описание");
        manager.addTask(task);
        manager.getTask(task.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNotNull(response.body(), "История не возвращена");
    }
}