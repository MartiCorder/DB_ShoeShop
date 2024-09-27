package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.exception.ServerErrorException;
import com.sun.net.httpserver.Request;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        var serverSocket = new ServerSocket(PORT);
        var router = new RequestRouter();

        while (true) {
            try (var clientSocket = serverSocket.accept()){
                var rawHttp = new RawHttp();
                var request = rawHttp.parseRequest(clientSocket.getInputStream());

                var response = router.execRequest(request);

                response.writeTo(clientSocket.getOutputStream());
            }
        }
    }
}
