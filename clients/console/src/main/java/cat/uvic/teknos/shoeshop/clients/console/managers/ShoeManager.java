package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;
import cat.uvic.teknos.shoeshop.models.Shoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ShoeManager {

    private RestClient restClient;
    private BufferedReader in;
    private PrintStream out;

    public ShoeManager(RestClient restClient) {
        this.restClient = restClient;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out);
    }

    public void start() throws IOException, RequestException {
        out.println("\n*** Shoe Management ***\n");

        String command;
        do {
            showShoeMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> getAllShoes();
                case "2" -> getShoeById();

            }
        } while (!command.equals("exit"));

        out.println("\n*** Exiting Shoe Management ***\n");
    }

    private void getAllShoes() throws IOException, RequestException {
        out.println("\n*** List of Shoes ***\n");

        var shoes = restClient.getAll("shoes", ShoeDto[].class);

        for (ShoeDto shoe : shoes) {
            out.println(shoe);
        }
    }

    private void getShoeById() throws IOException, RequestException {
        out.println("Enter Shoe ID:");
        var shoeId = readLine(in);

        var shoe = restClient.get("/shoes/" + shoeId, Shoe.class);
        out.println(shoe);
    }


    private void showShoeMenu() {
        out.println("\n*** Shoe Management Menu ***\n");
        out.println("1. Get All Shoes");
        out.println("2. Get Shoe by ID");
        out.println("3. Post New Shoe");
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
