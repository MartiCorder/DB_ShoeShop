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

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = new PrintStream(System.out);
    private static RestClient restClient = new RestClientImpl("localhost", 8080);

    private static ClientManager clientManager = new ClientManager(restClient, in);
    private static ShoeManager shoeManager = new ShoeManager(restClient);
    private static ShoeStoreManager shoeStoreManager = new ShoeStoreManager(restClient);

    public static void main(String[] args) throws IOException, RequestException {

        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> clientManager.start();
                case "2" -> shoeManager.start();
                case "3" -> shoeStoreManager.start();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Program Finished ***\n");

    }

    private static void showWelcomeMessage() {
        out.println("\n*** Welcome to the ShoeShop Back Office ***\n");
        out.println("Select a menu option:");
        out.println();
    }

    private static void showMainMenu() {
        out.println("\n*** Main Menu ***\n");
        out.println("1. Manage Client");
        out.println("2. Manage Shoe");
        out.println("3. Manage Shoe Store");
        out.println("\nType 'exit' to quit.");
    }

    private static String readLine(BufferedReader in) {
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the menu option", e);
        }
        return command;
    }
}
