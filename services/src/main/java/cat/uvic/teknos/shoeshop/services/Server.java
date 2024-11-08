package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.exception.ServerException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    public final int PORT = 8080;
    private final RequestRouter requestRouter;
    private volatile boolean shutdownServer = false;
    private final ExecutorService threadPool;
    private static final String PROPERTIES_PATH = "services/src/main/resources/server.properties";

    public Server(RequestRouter requestRouter, int maxThreads) {
        this.requestRouter = requestRouter;
        this.threadPool = Executors.newFixedThreadPool(maxThreads);
    }

    public void start() {

        ExecutorService scheduler = Executors.newSingleThreadExecutor();
        scheduler.submit(this::monitorShutdown);

        try (var serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciat en el port " + PORT);

            while (!shutdownServer) {
                try {
                    var clientSocket = serverSocket.accept();
                    threadPool.execute(() -> handleRequest(clientSocket));
                } catch (IOException e) {
                    if (shutdownServer) {
                        System.out.println("Servidor detingut.");
                    } else {
                        throw new ServerException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new ServerException(e);
        } finally {
            shutdown();
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (clientSocket) {
            var rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
            var request = rawHttp.parseRequest(clientSocket.getInputStream()).eagerly();

            var response = requestRouter.execRequest(request);
            response.writeTo(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    private void monitorShutdown() {
        try {
            while (!shutdownServer) {
                checkShutdown();
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void checkShutdown() {
        try (FileInputStream in = new FileInputStream(Paths.get(PROPERTIES_PATH).toFile())) {
            Properties properties = new Properties();
            properties.load(in);
            if ("true".equalsIgnoreCase(properties.getProperty("shutdown"))) {
                System.out.println("Apagant la aplicació...");
                System.exit(0);
            }
        } catch (IOException e) {
            System.err.println("Error al verificar l'arxiu de configuració: " + e.getMessage());
        }
    }

    private void shutdown() {
        threadPool.shutdownNow();
    }
}
