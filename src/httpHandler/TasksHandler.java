package httpHandler;

import adapter.GsonCreate;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
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
                    if (path.equals("/tasks")) {
                        List<Task> tasks = taskManager.getAllTasks();
                        String response = gson.toJson(tasks);
                        sendText(exchange, response, 200);
                    } else if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            String response = gson.toJson(task);
                            sendText(exchange, response, 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task newTask = gson.fromJson(body, Task.class);
                    if (newTask.getId() == 0) {
                        taskManager.addTask(newTask);
                        sendText(exchange, "{\"Сообщение\": \"Задача добавлена\"}", 201);
                    } else {
                        taskManager.updateTask(newTask);
                        sendText(exchange, "{\"Сообщение\": \"Задача обновлена \"}", 201);
                    }
                    break;
                case "DELETE":
                    if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.removeTask(id);
                        sendText(exchange, "{\"Сообщение\": \"Задача удалена\"}", 200);
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