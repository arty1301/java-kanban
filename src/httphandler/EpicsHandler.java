package httphandler;

import adapter.GsonCreate;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = GsonCreate.createGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/epics")) {
                        List<Epic> epics = taskManager.getAllEpics();
                        String response = gson.toJson(epics);
                        sendText(exchange, response, 200);
                    } else if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            String response = gson.toJson(epic);
                            sendText(exchange, response, 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic newEpic = gson.fromJson(body, Epic.class);
                    if (newEpic.getId() == 0) {
                        taskManager.addEpic(newEpic);
                        sendText(exchange, "{\"Сообщение\": \"Эпик добавлен\"}", 201);
                    } else {
                        taskManager.updateEpic(newEpic);
                        sendText(exchange, "{\"Сообщение\": \"Эпик обновлен\"}", 201);
                    }
                    break;
                case "DELETE":
                    if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.removeEpic(id);
                        sendText(exchange, "{\"Сообщение\": \"Эпик удален\"}", 200);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}