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
import java.io.PrintStream;

public class App {

    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintStream out = new PrintStream(System.out);

    private static final RestClient restClient = new RestClientImpl("localhost", 8080);

    private static final ClientManager clientManager = new ClientManager(restClient, in);
    private static final ShoeManager shoeManager = new ShoeManager(restClient);
    private static final ShoeStoreManager shoeStoreManager = new ShoeStoreManager(restClient);

    public static void main(String[] args) throws IOException, RequestException {

        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine();

            switch (command) {
                case "1" -> clientManager.start();
                case "2" -> shoeManager.start();
                case "3" -> shoeStoreManager.start();
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
        out.println("\nEscriu 'exit' per acabar el programma.");
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
}
