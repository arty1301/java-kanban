package httphandler;

import adapter.GsonCreate;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                    if (path.equals("/subtasks")) {
                        List<Subtask> subtasks = taskManager.getAllSubtasks();
                        String response = gson.toJson(subtasks);
                        sendText(exchange, response, 200);
                    } else if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask != null) {
                            String response = gson.toJson(subtask);
                            sendText(exchange, response, 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask newSubtask = gson.fromJson(body, Subtask.class);
                    if (newSubtask.getId() == 0) {
                        taskManager.addSubtask(newSubtask);
                        sendText(exchange, "{\"Сообщение\": \"Подзадача добавлена\"}", 201);
                    } else {
                        taskManager.updateSubtask(newSubtask);
                        sendText(exchange, "{\"Сообщение\": \"Подзадача обновлена\"}", 201);
                    }
                    break;
                case "DELETE":
                    if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.removeSubtask(id);
                        sendText(exchange, "{\"Сообщение\": \"подзадача удалена\"}", 200);
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