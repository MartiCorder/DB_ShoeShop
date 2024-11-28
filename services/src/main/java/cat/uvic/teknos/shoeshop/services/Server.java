package cat.uvic.teknos.shoeshop.services;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpOptions;
import rawhttp.core.RawHttpResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    public static final int PORT = 8080;
    private static final String PROPERTIES_FILE = "services/src/main/resources/server.properties";

    private final RequestRouter requestRouter;
    private final ExecutorService executor;
    private final ServerSocket serverSocket;
    private volatile boolean shutdownServer = false;

    public Server(RequestRouterImpl requestRouter) throws IOException {
        this.requestRouter = requestRouter;
        this.executor = Executors.newFixedThreadPool(3);
        this.serverSocket = new ServerSocket(PORT);
    }

    private boolean isShutdownRequested() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(input);
            return Boolean.parseBoolean(props.getProperty("SHUTDOWN_SERVER", "false"));
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            return false;
        }
    }

    private void closeServer() {
        try {
            System.out.println("Shutting down server...");
            shutdownServer = true;
            serverSocket.close();
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Executor service did not terminate in time. Forcing shutdown...");
                executor.shutdownNow();
            }
            System.out.println("Server closed correctly!");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during server shutdown: " + e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    public void start() {
        startShutdownMonitor();

        try {
            System.out.println("Server started on port " + PORT);
            while (!shutdownServer) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (shutdownServer) {
                System.out.println("Server stopped listening for connections.");
            } else {
                System.err.println("Server error: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket) {
            RawHttp rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
            var request = rawHttp.parseRequest(clientSocket.getInputStream());
            RawHttpResponse<?> response = requestRouter.execRequest(request);

            if (response != null) {
                response.writeTo(clientSocket.getOutputStream());
                clientSocket.getOutputStream().flush();
            } else {
                System.err.println("Null response generated for client request.");
            }
        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        }
    }

    private void startShutdownMonitor() {
        Thread shutdownMonitor = new Thread(() -> {
            while (!shutdownServer) {
                try {
                    Thread.sleep(5000);  // Check every 5 seconds
                    if (isShutdownRequested()) {
                        System.out.println("Shutdown request detected.");
                        closeServer();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Shutdown monitor interrupted.");
                }
            }
        });
        shutdownMonitor.setDaemon(true);
        shutdownMonitor.start();
    }
}
