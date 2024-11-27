package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.controllers.Controller;
import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.exception.ServerErrorException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Map;

public class RequestRouterImpl implements RequestRouter {
    private static final RawHttp rawHttp = new RawHttp();
    private final Map<String, Controller> controllers;
    private PrivateKey privateKey; // Clau privada per desxifrar la clau simètrica

    public RequestRouterImpl(Map<String, Controller> controllers) {
        this.controllers = controllers;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
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
                    responseJsonBody = handleGetRequest(controller, pathParts);
                    break;
                case "POST":
                    responseJsonBody = handlePostRequest(controller, request);
                    break;
                case "PUT":
                    responseJsonBody = handlePutRequest(controller, pathParts, request);
                    break;
                case "DELETE":
                    responseJsonBody = handleDeleteRequest(controller, pathParts);
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
            return rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n\r\n");
        } catch (ServerErrorException e) {
            return rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error\r\n\r\n");
        } catch (NumberFormatException e) {
            return rawHttp.parseResponse("HTTP/1.1 400 Bad Request\r\n\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String handleGetRequest(Controller controller, String[] pathParts) {
        if (pathParts.length == 2) {
            return controller.get();
        } else {
            var id = Integer.parseInt(pathParts[2]);
            return controller.get(id);
        }
    }

    private String handlePostRequest(Controller controller, RawHttpRequest request) throws IOException {
        if (!request.getBody().isPresent()) {
            throw new ServerErrorException("Body not present");
        }

        // Desxifrar dades POST
        var postJson = decryptRequestBody(request.getBody().get().decodeBodyToString(Charset.defaultCharset()));
        controller.post(postJson);

        return "{\"message\": \"Resource created successfully.\"}";
    }

    private String handlePutRequest(Controller controller, String[] pathParts, RawHttpRequest request) throws IOException {
        if (pathParts.length < 3 || !request.getBody().isPresent()) {
            throw new ServerErrorException("Invalid PUT request");
        }

        var putId = Integer.parseInt(pathParts[2]);
        var putJson = decryptRequestBody(request.getBody().get().decodeBodyToString(Charset.defaultCharset()));
        controller.put(putId, putJson);

        return "{\"message\": \"Resource updated successfully.\"}";
    }

    private String handleDeleteRequest(Controller controller, String[] pathParts) {
        if (pathParts.length < 3) {
            throw new ServerErrorException("Invalid DELETE request");
        }

        var deleteId = Integer.parseInt(pathParts[2]);
        controller.delete(deleteId);

        return "{\"message\": \"Resource deleted successfully.\"}";
    }

    private String decryptRequestBody(String encryptedBody) {
        try {
            // Desxifrar clau simètrica amb clau privada
            String[] parts = encryptedBody.split("::");  // Format: "encryptedKey::encryptedData"
            byte[] encryptedKey = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedData = Base64.getDecoder().decode(parts[1]);

            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedKey = rsaCipher.doFinal(encryptedKey);

            // Utilitzar clau simètrica per desxifrar les dades
            SecretKey secretKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedData = aesCipher.doFinal(encryptedData);
            return new String(decryptedData, Charset.defaultCharset());

        } catch (Exception e) {
            throw new ServerErrorException("Error desxifrant el cos de la petició", e);
        }
    }
}
