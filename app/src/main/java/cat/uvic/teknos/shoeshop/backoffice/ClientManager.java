package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

public class ClientManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ClientRepository clientRepository;


    private final ModelFactory modelFactory;

    public ClientManager(BufferedReader in, PrintStream out, ClientRepository clientRepository,
                          ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.clientRepository = clientRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        out.println("\n*** Client Management ***\n");

        var command = "";
        do {
            showClientMenu();
            try {
                command = in.readLine();
                switch (command) {
                    case "1" -> insertClient();
                    case "2" -> updateClient();
                    case "3" -> deleteClient();
                    case "4" -> getAllClients();
                    case "5" -> insertAddress();
                    case "6" -> insertShoeStore();
                }
            } catch (IOException | SQLException e) {
                out.println("Error reading input: " + e.getMessage());
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Client Management ***\n");
    }

    private void getAllClients() {
        out.println("\n List of Clients \n");
        try {
            var clients = clientRepository.getAll();

            out.println(AsciiTable.getTable(clients, Arrays.asList(
                    new Column().header("Id").with(client -> String.valueOf(client.getId())),
                    new Column().header("DNI").with(Client::getDni),
                    new Column().header("Name").with(Client::getName),
                    new Column().header("Phone").with(Client::getPhone)
            )));
        } catch (Exception e) {
            out.println("Error fetching clients: " + e.getMessage());
        }
    }
    private void deleteClient() {
        out.println("\n*** Delete Client ***\n");
        try {
            out.println("Enter the ID of the client to delete:");
            int id = Integer.parseInt(in.readLine());
            Client client = clientRepository.get(id);
            if (client != null) {
                clientRepository.delete(client);
                out.println("Client deleted successfully.");
            } else {
                out.println("Client with ID " + id + " not found.");
            }
        } catch (NumberFormatException | IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void updateClient() {
        out.println("\n*** Update Client ***\n");
        try {
            out.println("Enter the ID of the client to update:");
            int id = Integer.parseInt(in.readLine());
            Client client = clientRepository.get(id);
            if (client != null) {
                out.println("Enter new DNI:");
                String dni = in.readLine();
                out.println("Enter new Name:");
                String name = in.readLine();
                out.println("Enter new Phone:");
                String phone = in.readLine();

                // Validación de entrada
                if (dni.isBlank() || name.isBlank() || phone.isBlank()) {
                    out.println("Error: DNI, Name, and Phone cannot be empty.");
                    return;
                }

                client.setDni(dni);
                client.setName(name);
                client.setPhone(phone);
                clientRepository.save(client);
                out.println("Client updated successfully.");
            } else {
                out.println("Client with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            out.println("Error: Invalid client ID. Please enter a valid integer ID.");
        } catch (IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void insertClient() {
        out.println("\n*** Insert Client ***\n");
        try {
            Client client = modelFactory.createClient();
            out.println("Enter the DNI:");
            String dni = in.readLine();
            out.println("Enter the Name:");
            String name = in.readLine();
            out.println("Enter the Phone:");
            String phone = in.readLine();

            // Validación de entrada
            if (dni.isBlank() || name.isBlank() || phone.isBlank()) {
                out.println("Error: DNI, Name, and Phone cannot be empty.");
                return;
            }

            client.setDni(dni);
            client.setName(name);
            client.setPhone(phone);

            Address address = insertAddress();
            ShoeStore shoeStore = insertShoeStore();

            client.setAddresses(address);
            client.setShoeStores(shoeStore);

            clientRepository.save(client);
            out.println("Client inserted successfully.");
        } catch (IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private Address insertAddress() throws IOException, SQLException {
        out.println("\n*** Insert Address ***\n");
        Address address = modelFactory.createAddress();
        out.println("Enter the Location:");
        address.setLocation(in.readLine());
        out.println("Address inserted successfully.");
        return address;
    }

    private ShoeStore insertShoeStore() throws IOException, SQLException {
        out.println("\n*** Insert Shoe Store ***\n");
        ShoeStore shoeStore = modelFactory.createShoeStore();
        out.println("Enter the Name:");
        shoeStore.setName(in.readLine());
        out.println("Enter the Owner:");
        shoeStore.setOwner(in.readLine());
        out.println("Enter the Location:");
        shoeStore.setLocation(in.readLine());
        out.println("Shoe Store inserted successfully.");
        return shoeStore;
    }

    private void showClientMenu() {
        out.println("\n*** Client Management Menu ***\n");
        out.println("1. Insert Client");
        out.println("2. Update Client");
        out.println("3. Delete Client");
        out.println("4. Get All Clients");
        out.println("5. Insert Address");
        out.println("6. Insert Shoe Store");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
