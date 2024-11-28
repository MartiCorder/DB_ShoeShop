package cat.uvic.teknos.shoeshop.clients.console;

import cat.uvic.teknos.shoeshop.clients.console.managers.ClientManager;
import cat.uvic.teknos.shoeshop.clients.console.managers.ShoeManager;
import cat.uvic.teknos.shoeshop.clients.console.managers.ShoeStoreManager;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.io.FileNotFoundException;
import java.util.Properties;

public class App {

    private static final PublicKey publicServerKey;
    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintStream out = System.out;
    private static final RestClientImpl restClient;

    static {
        try {
            // Cargar propiedades desde el archivo app.properties
            Properties properties = new Properties();
            var propertiesStream = App.class.getResourceAsStream("/app.properties");
            if (propertiesStream == null) {
                throw new FileNotFoundException("Error: No se pudo encontrar el archivo de configuración 'app.properties'.");
            }
            properties.load(propertiesStream);

            // Leer host, puerto y ruta del certificado desde app.properties
            String host = properties.getProperty("server.host", "localhost");
            int port = Integer.parseInt(properties.getProperty("server.port", "8080"));
            String certPath = properties.getProperty("server.cert.path", "/server.cert");

            // Cargar clave pública del servidor desde el certificado
            publicServerKey = loadPublicKeyFromCert(certPath);
            restClient = new RestClientImpl(host, port, publicServerKey);
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la aplicación: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws IOException, RequestException {

        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine();

            switch (command) {
                case "1" -> new ClientManager(restClient, in).start();
                case "2" -> new ShoeManager(restClient).start();
                case "3" -> new ShoeStoreManager(restClient).start();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Programma tancat correctament ***\n");

    }

    private static void showWelcomeMessage() {
        out.println("\n*** Benvinguts a ShoeShop Back Office ***\n");
        out.println("Selecciona una opció:");
        out.println();
    }

    private static void showMainMenu() {
        out.println("\n*** Menu Principal ***\n");
        out.println("1. Client");
        out.println("2. Shoe");
        out.println("3. Shoe Store");
        out.println("\nEscriu 'exit' per acabar el programa.");
    }

    private static String readLine() {
        String command;
        try {
            command = App.in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al llegir la opció.", e);
        }
        return command;
    }

    // Método para cargar la clave pública desde un archivo dentro del proyecto
    public static PublicKey loadPublicKeyFromCert(String certFile) throws Exception {
        try (InputStream certStream = App.class.getResourceAsStream(certFile)) {
            if (certStream == null) {
                throw new FileNotFoundException("Error: No se encontró el archivo del certificado en la ruta especificada: " + certFile);
            }
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certStream);

            // Obtener la clave pública del certificado
            return certificate.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Error cargando la clave pública desde el archivo: " + certFile, e);
        }
    }
}
