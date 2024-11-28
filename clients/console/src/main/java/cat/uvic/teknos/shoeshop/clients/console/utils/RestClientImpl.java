package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

public class RestClientImpl implements RestClient {
    private final int port;
    private final String host;
    private final PublicKey serverPublicKey;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public RestClientImpl(String host, int port, PublicKey serverPublicKey) {
        this.host = host;
        this.port = port;
        this.serverPublicKey = serverPublicKey;
    }

    @Override
    public <T> T get(String path, Class<T> returnType) throws RequestException {
        return execRequest("GET", path, null, returnType);
    }

    @Override
    public <T> T[] getAll(String path, Class<T[]> returnType) throws RequestException {
        return execRequest("GET", path, null, returnType);
    }

    @Override
    public void post(String path, String body) throws RequestException {
        execRequest("POST", path, body, Void.class);
    }

    @Override
    public void put(String path, String body) throws RequestException {
        execRequest("PUT", path, body, Void.class);
    }

    @Override
    public void delete(String path, String body) throws RequestException {
        execRequest("DELETE", path, body, Void.class);
    }

    /**
     * Executa la petició HTTP segons el mètode (GET, POST, etc.) i processa la resposta.
     *
     * @param method       Mètode HTTP (GET, POST, etc.)
     * @param path         Ruta de la petició
     * @param body         Cos de la petició (si s'aplica)
     * @param returnType   Tipus de resposta esperat
     * @param <T>          Tipus de la resposta
     * @return             Resposta del servidor
     * @throws RequestException Si ocorre un error a l'executar la petició
     */
    protected <T> T execRequest(String method, String path, String body, Class<T> returnType) throws RequestException {
        var rawHttp = new RawHttp();

        try (var socket = new Socket(host, port)) {
            if (body == null) {
                body = ""; // Si no es proporciona un cos, es fa buit
            }

            // Crear clau simètrica per xifrar el cos de la petició
            SecretKey symmetricKey = CryptoUtils.createSecretKey();
            String encryptedBody = CryptoUtils.encrypt(body, symmetricKey);

            // Calcular el hash del cos xifrat
            String bodyHash = Base64.getEncoder().encodeToString(CryptoUtils.hash(encryptedBody.getBytes()));

            // Xifrar la clau simètrica amb la clau pública del servidor
            String encryptedSymmetricKey = CryptoUtils.asymmetricEncrypt(
                    Base64.getEncoder().encodeToString(symmetricKey.getEncoded()), serverPublicKey);

            // Construir la petició HTTP amb els headers necessaris
            RawHttpRequest request = rawHttp.parseRequest(
                    method + " http://" + host + ":" + port + "/" + path + " HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "User-Agent: RawHTTP\r\n" +
                            "Content-Type: application/json\r\n" +
                            "X-Symmetric-Key: " + encryptedSymmetricKey + "\r\n" +
                            "X-Body-Hash: " + bodyHash + "\r\n" +
                            "Content-Length: " + encryptedBody.length() + "\r\n" +
                            "\r\n" +
                            encryptedBody
            );

            // Enviar la petició
            request.writeTo(socket.getOutputStream());

            // Llegir la resposta del servidor
            RawHttpResponse response = rawHttp.parseResponse(socket.getInputStream()).eagerly();

            // Comprovar l'estat de la resposta
            int statusCode = response.getStatusCode();
            if (statusCode != 200) {
                throw new RequestException("Unexpected response status: " + statusCode);
            }

            // Llegir i desxifrar el cos de la resposta
            String encryptedResponseBody = response.getBody()
                    .map(bodyPart -> {
                        try {
                            return new String(bodyPart.decodeBody(), "UTF-8");
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to decode response body", e);
                        }
                    })
                    .orElse("");

            if (encryptedResponseBody.isEmpty()) {
                throw new RequestException("Empty response body.");
            }

            // Validar el hash de la resposta
            String responseHash = response.getHeaders().getFirst("X-Body-Hash").orElse("");
            if (responseHash.isEmpty()) {
                throw new RequestException("Missing response hash header.");
            }

            // Calcular el hash del cos xifrat de la resposta
            String calculatedHash = Base64.getEncoder().encodeToString(CryptoUtils.hash(encryptedResponseBody.getBytes()));

            if (!calculatedHash.equals(responseHash)) {
                throw new RequestException("Invalid hash for the response body. Expected: " + responseHash + ", but got: " + calculatedHash);
            }

            // Desxifrar el cos de la resposta
            String decryptedResponseBody = CryptoUtils.decrypt(encryptedResponseBody, symmetricKey);

            // Processar la resposta en el tipus esperat
            if (returnType != Void.class) {
                return MAPPER.readValue(decryptedResponseBody, returnType);
            }

            return null;

        } catch (IOException e) {
            throw new RequestException("Request failed: " + e.getMessage(), e);
        }
    }
}
