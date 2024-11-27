package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.ConsoleClientException;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import rawhttp.core.RawHttp;

import java.io.IOException;
import java.net.Socket;


public class RestClientImpl implements RestClient {
    private final int port;
    private final String host;

    public RestClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public <T> T get(String path, Class<T> returnType, BodyDecoder decoder,HeaderEntry... entries) throws RequestException {
        return execRequest("GET", path, null, returnType, decoder, null, entries);
    }

    @Override
    public <T> T[] getAll(String path, Class<T[]> returnType, BodyDecoder decoder, HeaderEntry... entries) throws RequestException {
        return execRequest("GET", path, null, returnType, decoder, null, entries);
    }

    @Override
    public void post(String path, String body, BodyEncoder encoder,HeaderEntry... entries) throws RequestException {
        execRequest("POST", path, body, Void.class, null, encoder, entries);
    }

    @Override
    public void put(String path, String body, BodyEncoder encoder, HeaderEntry... entries) throws RequestException {
        execRequest("PUT", path, body, Void.class, null, encoder, entries);
    }
    @Override
    public void delete(String path, String body, HeaderEntry... entries) throws RequestException {
        execRequest("DELETE", path, body, Void.class, null, null, entries);
    }

    protected <T> T execRequest(String method, String path, String body, Class<T> returnType, BodyDecoder decoder, BodyEncoder encoder, HeaderEntry... entries) throws RequestException {
        var rawHttp = new RawHttp();
        try (var socket = new Socket(host, port)) {
            if (body == null) {
                body = "";
            } else {
                body = encoder != null ? encoder.encode(body) : body;
            }

            var request = rawHttp.parseRequest(
                    method + " " + String.format("http://%s:%d/%s", host, port, path) + " HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "User-Agent: RawHTTP\r\n" +
                            "Content-Length: " + body.length()+ "\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Accept: application/json\r\n" +
                            addHeaders(entries) +
                            "\r\n" +
                            body
            );

            request.writeTo(socket.getOutputStream());

            T returnValue = null;
            var response = rawHttp.parseResponse(socket.getInputStream()).eagerly();
            if (!returnType.isAssignableFrom(Void.class)) {
                var responseBody = decoder != null ? decoder.decode(response.getBody().get().toString()) : response.getBody().get().toString();
                returnValue = Mappers.get().readValue(responseBody, returnType);
            }

            return returnValue;
        } catch (IOException e) {
            throw new ConsoleClientException();
        }
    }

    private String addHeaders(HeaderEntry[] entries) {
        var headers = new StringBuilder();
        for (var entry : entries) {
            headers.append(entry.getKey()).append(": ").append(entry.getValue()).append("\\r\\n");
        }

        return headers.toString();
    }
}
