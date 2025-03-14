import adapter.GsonCreate;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerEpicsTest extends HttpTaskManagerTestBase {
    private final Gson gson = GsonCreate.createGson();

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic();
        epic.setTitle("Эпик");
        epic.setDescription("Описание");

        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код ответа");
        assertEquals(1, manager.getAllEpics().size(), "Неверное количество эпиков");
    }
}