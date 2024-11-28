package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.controllers.Controller;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;

public class RequestRouterImpl implements RequestRouter {

    private static final RawHttp rawHttp = new RawHttp();
    private final Map<String, Controller> controllers;
    private final PrivateKey privateKey;

    public RequestRouterImpl(Map<String, Controller> controllers) {
        this.controllers = controllers;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(RequestRouterImpl.class.getResourceAsStream("/server.p12"), "Teknos01.".toCharArray());
            this.privateKey = (PrivateKey) keyStore.getKey("server", "Teknos01.".toCharArray());
        } catch (KeyStoreException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException |
                 CertificateException e) {
            throw new RuntimeException("Couldn't load key (RequestRouterImpl): ", e);
        }
    }

    @Override
    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        try {
            System.out.println("Processing request: " + request.getMethod() + " " + request.getUri().getPath());

            String encryptedKeyBase64 = request.getHeaders()
                    .get("X-Symmetric-Key")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing 'X-Symmetric-Key' header"));
            System.out.println("Got encrypted symmetric key: " + encryptedKeyBase64);

            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedKeyBytes = rsaCipher.doFinal(CryptoUtils.fromBase64(encryptedKeyBase64));

            if (decryptedKeyBytes.length != 32) {
                throw new RuntimeException("Decrypted key length is incorrect. Expected 32 bytes for AES-256, but got " + decryptedKeyBytes.length);
            }

            SecretKey symmetricKey = new SecretKeySpec(decryptedKeyBytes, "AES");
            System.out.println("Symmetric key decrypted successfully");

            String decryptedBody = "";
            if (request.getBody().isPresent()) {
                String encryptedBody = request.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
                if (!encryptedBody.isEmpty()) {
                    decryptedBody = CryptoUtils.decrypt(encryptedBody, symmetricKey);

                    String providedHash = request.getHeaders()
                            .get("X-Body-Hash")
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Missing 'X-Body-Hash' header"));
                    String calculatedHash = CryptoUtils.getHash(encryptedBody);

                    if (!calculatedHash.equals(providedHash)) {
                        throw new RuntimeException("Body hash mismatch: Possible tampered data");
                    }
                }
            }

            String responseBody = handleRequest(request.getMethod(), request.getUri().getPath(), decryptedBody);

            String encryptedResponse = CryptoUtils.encrypt(responseBody, symmetricKey);
            String responseHash = CryptoUtils.getHash(encryptedResponse);

            return rawHttp.parseResponse(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: " + encryptedResponse.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                            "X-Body-Hash: " + responseHash + "\r\n" +
                            "\r\n" +
                            encryptedResponse
            );

        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return rawHttp.parseResponse(
                    "HTTP/1.1 500 Internal Server Error\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + e.getMessage().getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                            "\r\n" +
                            e.getMessage()
            );
        }
    }

    private String handleRequest(String method, String path, String body) throws Exception {
        String[] pathParts = path.split("/");
        if (pathParts.length < 2) {
            throw new RuntimeException("Invalid path: " + path);
        }

        Controller controller = controllers.get(pathParts[1]);
        if (controller == null) {
            throw new RuntimeException("Controller not found for path: " + pathParts[1]);
        }

        switch (method) {
            case "POST":
                controller.post(body);
                return "{}";
            case "GET":
                if (pathParts.length == 2) {
                    return controller.get();
                } else if (pathParts.length == 3) {
                    return controller.get(Integer.parseInt(pathParts[2]));
                }
                break;
            case "PUT":
                if (pathParts.length == 3) {
                    controller.put(Integer.parseInt(pathParts[2]), body);
                    return "{}";
                }
                break;
            case "DELETE":
                if (pathParts.length == 3) {
                    controller.delete(Integer.parseInt(pathParts[2]));
                    return "{}";
                }
                break;
            default:
                throw new RuntimeException("Unsupported method: " + method);
        }

        throw new RuntimeException("Invalid path or method combination");
    }
}
