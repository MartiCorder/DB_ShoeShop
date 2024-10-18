package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.controllers.Controller;
import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.exception.ServerErrorException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class RequestRouterImpl implements RequestRouter {
    private static RawHttp rawHttp = new RawHttp();
    private final Map<String, Controller> controllers;

    public RequestRouterImpl(Map<String, Controller> controllers) {
        this.controllers = controllers;
    }

    /**
     *
     *
     * @param request
     * @return the http response to send to the client
     */
    @Override
    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        var path = request.getUri().getPath();
        var method = request.getMethod();
        var pathParts = path.split("/");

        // Validem que tinguem un controlador per aquesta ruta
        if (pathParts.length < 2) {
            return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
        }

        // Obtenim el nom del controlador i busquem el controlador associat
        var controllerName = pathParts[1];
        var controller = controllers.get(controllerName);

        if (controller == null) {
            // Retornem un 404 si no trobem el controlador
            return rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n\r\n");
        }

        String responseJsonBody = "";

        try {
            // Gestionem les diferents operacions HTTP basades en el mètode
            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        // GET /clients
                        responseJsonBody = controller.get();
                    } else {
                        // GET /clients/{id}
                        var id = Integer.parseInt(pathParts[2]);
                        responseJsonBody = controller.get(id);
                    }
                    break;

                case "POST":
                    // POST /clients
                    var clientJson = request.getBody().get().asString(); // Obtenim el cos de la petició
                    controller.post(clientJson);
                    responseJsonBody = "{\"message\": \"Resource created successfully.\"}";
                    break;

                case "PUT":
                    if (pathParts.length < 3) {
                        return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
                    }
                    // PUT /clients/{id}
                    var clientId = Integer.parseInt(pathParts[2]);
                    var updateJson = request.getBody().get().asString();
                    controller.put(clientId, updateJson);
                    responseJsonBody = "{\"message\": \"Resource updated successfully.\"}";
                    break;

                case "DELETE":
                    if (pathParts.length < 3) {
                        return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
                    }
                    // DELETE /clients/{id}
                    var deleteId = Integer.parseInt(pathParts[2]);
                    controller.delete(deleteId);
                    responseJsonBody = "{\"message\": \"Resource deleted successfully.\"}";
                    break;

                default:
                    // Si el mètode HTTP no és suportat, retornem un 405
                    return rawHttp.parseResponse("HTTP/1.1 405 Method Not Allowed\r\n\r\n");
            }

            // Retornem una resposta HTTP amb èxit (200 OK)
            RawHttpResponse<?> response = rawHttp.parseResponse(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: " + responseJsonBody.length() + "\r\n" +
                            "\r\n" +
                            responseJsonBody
            );
            return response;

        } catch (ResourceNotFoundException e) {
            // Retornem 404 si no es troba el recurs
            return rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n\r\n");

        } catch (ServerErrorException e) {
            // Retornem 500 si hi ha un error intern del servidor
            return rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error\r\n\r\n");

        } catch (JsonProcessingException | NumberFormatException e) {
            // Si hi ha algun error en processar el JSON o el format de l'ID
            return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
        }
    }

    private String manageClients(RawHttpRequest request, String method, String[] pathParts, String responseJsonBody) {
        var controller = controllers.get(pathParts[1]);

        if (method.equals("POST")) {
            var clientJson = request.getBody().get().toString();
            controller.post(clientJson);

        } else if (method.equals("GET") && pathParts.length == 2) {
            responseJsonBody = controller.get();

        } else if (method.equals("DELETE")) {
            var clientId = Integer.parseInt(pathParts[2]);
            controller.delete(clientId);
        } else if (method.equals("PUT")) {
            var clientId = Integer.parseInt(pathParts[2]);
            var mapper = new ObjectMapper();

            var studentJson = request.getBody().get().toString();
            controller.put(clientId, studentJson);

        }
        return responseJsonBody;
    }
}
