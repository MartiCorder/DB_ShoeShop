package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.exception.ServerException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpOptions;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 8080;
    private final RequestRouter requestRouter;
    private static boolean SHUTDOWN_SERVER = false;

    public Server(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    public void start() {
        try (var serverSocket = new ServerSocket(PORT)) {
            while (!SHUTDOWN_SERVER) {
                try (var clientSocket = serverSocket.accept()) {
                    var rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
                    var request = rawHttp.parseRequest(clientSocket.getInputStream());

                    RawHttpResponse<?> response = requestRouter.execRequest(request);

                    response.writeTo(clientSocket.getOutputStream());
                } catch (Exception e) {
                    throw new ServerException(e);
                }
            }
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }
}