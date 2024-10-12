package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.exception.ServerException;

import rawhttp.core.RawHttp;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public final int PORT = 8080;
    private final RequestRouter requestRouter;
    private boolean SHUTDOWN_SERVER;

    public Server(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    public void start() {
        try(var serverSocket = new ServerSocket(PORT)) {

            while (!SHUTDOWN_SERVER) {
                try (var clientSocket = serverSocket.accept()){
                    var rawHttp = new RawHttp();
                    var request = rawHttp.parseRequest(clientSocket.getInputStream());

                    var response = requestRouter.execRequest(request);

                    response.writeTo(clientSocket.getOutputStream());
                }
            }
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }
}
