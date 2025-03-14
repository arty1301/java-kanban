package httpHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"Ошибка\": \"Не найдено\"}", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"Ошибка\": \"Задача пересекается\"}", 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"Ошибка\": \"Ошибка сервера\"}", 500);
    }
}
