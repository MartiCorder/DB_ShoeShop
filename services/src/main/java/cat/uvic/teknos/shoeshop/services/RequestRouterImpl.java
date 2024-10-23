package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.controllers.Controller;
import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.exception.ServerErrorException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class RequestRouterImpl implements RequestRouter {
    private static final RawHttp rawHttp = new RawHttp(); // Fer-ho final
    private final Map<String, Controller> controllers;

    public RequestRouterImpl(Map<String, Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        var path = request.getUri().getPath();
        var method = request.getMethod();
        var pathParts = path.split("/");

        if (pathParts.length < 2) {
            return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
        }

        var controllerName = pathParts[1];
        var controller = controllers.get(controllerName);

        if (controller == null) {
            return rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n\r\n");
        }

        String responseJsonBody = "";

        try {
            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        responseJsonBody = controller.get();
                    } else {
                        var id = Integer.parseInt(pathParts[2]);
                        responseJsonBody = controller.get(id);
                    }
                    break;

                case "POST":
                    if (!request.getBody().isPresent()) {
                        return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
                    }
                    var postJson = request.getBody().get().decodeBodyToString(Charset.defaultCharset());
                    controller.post(postJson);
                    responseJsonBody = "{\"message\": \"Resource created successfully.\"}";
                    break;

                case "PUT":
                    if (pathParts.length < 3 || !request.getBody().isPresent()) {
                        return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
                    }
                    var putId = Integer.parseInt(pathParts[2]);
                    var putJson = request.getBody().get().decodeBodyToString(Charset.defaultCharset());
                    controller.put(putId, putJson);
                    responseJsonBody = "{\"message\": \"Resource updated successfully.\"}";
                    break;

                case "DELETE":
                    if (pathParts.length < 3) {
                        return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
                    }
                    var deleteId = Integer.parseInt(pathParts[2]);
                    controller.delete(deleteId);
                    responseJsonBody = "{\"message\": \"Resource deleted successfully.\"}";
                    break;

                default:
                    return rawHttp.parseResponse("HTTP/1.1 405 Method Not Allowed\r\n\r\n");
            }
            return rawHttp.parseResponse(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: " + responseJsonBody.length() + "\r\n" +
                            "\r\n" +
                            responseJsonBody
            );

        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n\r\n");

        } catch (ServerErrorException e) {
            e.printStackTrace();
            return rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error\r\n\r\n");

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
        }
    }
}