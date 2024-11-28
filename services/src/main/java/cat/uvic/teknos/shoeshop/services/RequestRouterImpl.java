package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.controllers.Controller;
import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;

public class RequestRouterImpl implements RequestRouter {
    private static final RawHttp rawHttp = new RawHttp();
    private final Map<String, Controller> controllers;
    private final PrivateKey serverPrivateKey; // Clau privada del servidor per desxifrar les claus simètriques

    public RequestRouterImpl(Map<String, Controller> controllers, PrivateKey serverPrivateKey) {
        this.controllers = controllers;
        this.serverPrivateKey = serverPrivateKey;
    }

    @Override
    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        var path = request.getUri().getPath();
        var pathParts = path.split("/");

        if (pathParts.length < 3) {
            return createJsonErrorResponse(404, "Controller not found.");
        }

        var controllerName = pathParts[2];
        var method = request.getMethod();
        String responseJsonBody = "";

        try {
            var controller = controllers.get(controllerName);
            if (controller == null) {
                throw new ResourceNotFoundException("Controller not found: " + controllerName);
            }

            // Recuperar la clau simètrica xifrada de l'encapçalament
            List<String> symmetricKeyHeader = request.getHeaders().get("X-Symmetric-Key");
            if (symmetricKeyHeader == null || symmetricKeyHeader.isEmpty()) {
                return createJsonErrorResponse(400, "Missing X-Symmetric-Key header.");
            }

            String encryptedSymmetricKeyBase64 = symmetricKeyHeader.get(0);
            byte[] encryptedSymmetricKey = Base64.getDecoder().decode(encryptedSymmetricKeyBase64);

            // Desxifrar la clau simètrica amb la clau privada del servidor
            SecretKey symmetricKey = CryptoUtils.decodeSecretKey(
                    CryptoUtils.asymmetricDecrypt(Arrays.toString(encryptedSymmetricKey), serverPrivateKey)
            );

            // Recuperar i verificar el hash del cos xifrat
            List<String> bodyHashHeader = request.getHeaders().get("X-Body-Hash");
            if (bodyHashHeader == null || bodyHashHeader.isEmpty()) {
                return createJsonErrorResponse(400, "Missing X-Body-Hash header.");
            }

            String bodyHash = bodyHashHeader.get(0);
            byte[] encryptedBody = request.getBody().map(body -> {
                try {
                    return body.decodeBody();
                } catch (IOException e) {
                    throw new RuntimeException("Error decoding body", e);
                }
            }).orElse(new byte[0]);
            String calculatedHash = CryptoUtils.getHash(new String(encryptedBody));

            if (!calculatedHash.equals(bodyHash)) {
                return createJsonErrorResponse(400, "Invalid hash for the request body.");
            }

            // Desxifrar el cos amb la clau simètrica
            String decryptedBody = CryptoUtils.decrypt(new String(encryptedBody), symmetricKey);

            // Processar la sol·licitud segons el mètode HTTP
            if ("POST".equals(method)) {
                controller.post(decryptedBody);
            } else if ("GET".equals(method)) {
                if (pathParts.length == 4) {
                    var resourceId = Integer.parseInt(pathParts[3]);
                    responseJsonBody = controller.get(resourceId);
                } else {
                    responseJsonBody = controller.get();
                }
            } else if ("PUT".equals(method)) {
                if (pathParts.length < 4) {
                    return createJsonErrorResponse(400, "Resource ID is required for PUT requests.");
                }
                var resourceId = Integer.parseInt(pathParts[3]);
                controller.put(resourceId, decryptedBody);
            } else if ("DELETE".equals(method)) {
                if (pathParts.length < 4) {
                    return createJsonErrorResponse(400, "Resource ID is required for DELETE requests.");
                }
                var resourceId = Integer.parseInt(pathParts[3]);
                controller.delete(resourceId);
            } else {
                return createJsonErrorResponse(405, "Method not allowed.");
            }

            if (responseJsonBody.isEmpty()) {
                responseJsonBody = "{\"message\": \"No content available\"}";
            }

            // Cifrar la resposta utilitzant la clau simètrica
            String encryptedResponseBody = CryptoUtils.encrypt(responseJsonBody, symmetricKey);
            String responseHash = CryptoUtils.getHash(encryptedResponseBody);

            return rawHttp.parseResponse(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "X-Body-Hash: " + responseHash + "\r\n" +
                            "Content-Length: " + encryptedResponseBody.length() + "\r\n" +
                            "\r\n" +
                            encryptedResponseBody
            );


        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace(); // Mostrar més detalls del error
            return createJsonErrorResponse(500, "Error processing request: " + e.getMessage());
        }

    }

    private RawHttpResponse<?> createJsonErrorResponse(int statusCode, String message) {
        String jsonResponse = "{\"error\": \"" + message + "\"}";
        return rawHttp.parseResponse(
                "HTTP/1.1 " + statusCode + " " + (statusCode == 404 ? "Not Found" : "Error") + "\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + jsonResponse.length() + "\r\n" +
                        "\r\n" +
                        jsonResponse
        );
    }
}