package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ShoeStoreManager {

    private RestClient restClient;
    private BufferedReader in;
    private PrintStream out;

    public ShoeStoreManager(RestClient restClient) {
        this.restClient = restClient;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out);
    }

    public void start() throws IOException, RequestException {

    }





    private void showStoreMenu() {
        out.println("\n*** Shoe Store Management Menu ***\n");
        out.println("1. Get All Shoe Stores");
        out.println("2. Get Shoe Store by ID");
        out.println("3. Post New Shoe Store");
        out.println("4. Update Shoe Store");
        out.println("5. Delete Shoe Store");
        out.println("Type 'exit' to quit.");
        out.println();
    }

    private String readLine(BufferedReader in) {
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the menu option", e);
        }
        return command;
    }
}
