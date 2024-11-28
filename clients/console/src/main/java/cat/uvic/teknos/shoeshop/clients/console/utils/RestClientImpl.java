package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import javax.crypto.*;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class RestClientImpl implements RestClient {
    private final int port;
    private final String host;
    private static final int SOCKET_TIMEOUT = 10000;
    private final PublicKey publicKey;
    private final Certificate serverCertificate;

    public RestClientImpl(String host, int port) {
        this.port = port;
        this.host = host;
        System.out.println("RestClient initialized with host: " + host + ", port: " + port);

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(RestClientImpl.class.getResourceAsStream("/client1.p12"), "Teknos01.".toCharArray());
            serverCertificate = keyStore.getCertificate("server");
            publicKey = serverCertificate.getPublicKey();
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            throw new RuntimeException("Failed to load client keystore: ", e);
        }
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

    private <T> T execRequest(String method, String path, String body, Class<T> returnType) throws RequestException {
        RawHttp rawHttp = new RawHttp();

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(SOCKET_TIMEOUT);
            System.out.println("Connected to " + host + ":" + port);

            SecretKey symmetricKey = CryptoUtils.createSecretKey();
            String encryptedKey = encryptKey(symmetricKey);

            body = (body == null) ? "" : body;
            String encryptedBody = body.isEmpty() ? "" : CryptoUtils.encrypt(body, symmetricKey);
            String hash = body.isEmpty() ? "" : CryptoUtils.getHash(encryptedBody);

            String requestString = buildRequestString(method, path, encryptedBody, encryptedKey, hash);
            RawHttpRequest request = rawHttp.parseRequest(requestString);
            request.writeTo(socket.getOutputStream());
            socket.getOutputStream().flush();
            System.out.println("Request sent:\n" + requestString);

            RawHttpResponse<?> response = rawHttp.parseResponse(socket.getInputStream()).eagerly();
            System.out.println("Response received with status: " + response.getStatusCode());

            if (response.getStatusCode() != 200) {
                throw new RequestException("Server returned error status: " + response.getStatusCode());
            }

            if (returnType.equals(Void.class)) return null;

            String encryptedResponse = response.getBody()
                    .map(bodyPart -> {
                        try {
                            return bodyPart.asRawString(StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to read response body", e);
                        }
                    }).orElse("{}");

            String decryptedResponse = CryptoUtils.decrypt(encryptedResponse, symmetricKey);
            return new ObjectMapper().readValue(decryptedResponse, returnType);

        } catch (SocketTimeoutException e) {
            throw new RequestException("Server response timed out", e);
        } catch (IOException | GeneralSecurityException e) {
            throw new RequestException("Request execution failed: " + e.getMessage(), e);
        }
    }

    private String encryptKey(SecretKey symmetricKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return CryptoUtils.toBase64(cipher.doFinal(symmetricKey.getEncoded()));
    }

    private String buildRequestString(String method, String path, String body, String encryptedKey, String hash) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return String.format(
                "%s http://%s:%d/%s HTTP/1.1\r\n" +
                        "Host: %s\r\n" +
                        "User-Agent: RawHTTP\r\n" +
                        "Content-Length: %d\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Accept: application/json\r\n" +
                        "X-Symmetric-Key: %s\r\n" +
                        "X-Body-Hash: %s\r\n" +
                        "Connection: close\r\n\r\n%s",
                method, host, port, path, host, body.length(), encryptedKey, hash, body
        );
    }
}
