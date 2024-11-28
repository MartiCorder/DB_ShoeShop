package cat.uvic.teknos.shoeshop.clients.console;

import cat.uvic.teknos.shoeshop.clients.console.managers.ClientManager;
import cat.uvic.teknos.shoeshop.clients.console.managers.ShoeManager;
import cat.uvic.teknos.shoeshop.clients.console.managers.ShoeStoreManager;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class App {

    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintStream out = System.out;
    private static final RestClientImpl restClient = new RestClientImpl("localhost", 8080);

    public static void main(String[] args) {
        try {
            showWelcomeMessage();

            String command = "";
            do {
                showMainMenu();
                command = readLine();

                switch (command) {
                    case "1":
                        new ClientManager(restClient, in).start();
                        break;
                    case "2":
                        new ShoeManager(restClient).start();
                        break;
                    case "3":
                        new ShoeStoreManager(restClient).start();
                        break;
                    case "exit":
                        out.println("\n*** Programma tancat correctament ***\n");
                        break;
                    default:
                        out.println("Comanda no vàlida. Si us plau, selecciona una opció vàlida.");
                }
            } while (!command.equals("exit"));

        } catch (IOException | RequestException e) {
            out.println("\n*** Error en executar el programa: " + e.getMessage() + " ***\n");
        }
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
            command = in.readLine();
            if (command == null || command.trim().isEmpty()) {
                throw new IOException("No s'ha introduït cap dada.");
            }
        } catch (IOException e) {
            out.println("Error al llegir l'entrada. Si us plau, intenta-ho de nou.");
            throw new RuntimeException(e);
        }
        return command.trim();
    }
}
