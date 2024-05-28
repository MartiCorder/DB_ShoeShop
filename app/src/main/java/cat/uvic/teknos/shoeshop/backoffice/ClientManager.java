package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Set;

public class ClientManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ClientRepository clientRepository;
    private final ModelFactory modelFactory;

    public ClientManager(BufferedReader in, PrintStream out, ClientRepository clientRepository, ModelFactory modelFactory) {
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
                    case "1" -> insert();
                    case "2" -> update();
                    case "3" -> delete();
                    case "4" -> getAll();
                }
            } catch (IOException e) {
                out.println("Error reading input: " + e.getMessage());
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Client Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Clients ***\n");
        try {
            Set<Client> clients = clientRepository.getAll();
            clients.forEach(client -> out.println(client.toString()));
        } catch (Exception e) {
            out.println("Error fetching clients: " + e.getMessage());
        }
    }

    private void delete() {
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

    private void update() {
        out.println("\n*** Update Client ***\n");
        try {
            out.println("Enter the ID of the client to update:");
            int id = Integer.parseInt(in.readLine());
            Client client = clientRepository.get(id);
            if (client != null) {
                out.println("Enter new DNI:");
                client.setDni(in.readLine());
                out.println("Enter new Name:");
                client.setName(in.readLine());
                out.println("Enter new Phone:");
                client.setPhone(in.readLine());
                clientRepository.save(client);
                out.println("Client updated successfully.");
            } else {
                out.println("Client with ID " + id + " not found.");
            }
        } catch (NumberFormatException | IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void insert() {
        out.println("\n*** Insert Client ***\n");
        try {
            Client client = modelFactory.createClient();
            out.println("Enter the DNI:");
            client.setDni(in.readLine());
            out.println("Enter the Name:");
            client.setName(in.readLine());
            out.println("Enter the Phone:");
            client.setPhone(in.readLine());
            clientRepository.save(client);
            out.println("Client inserted successfully.");
        } catch (IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void showClientMenu() {
        out.println("\n*** Client Management Menu ***\n");
        out.println("1. Insert Client");
        out.println("2. Update Client");
        out.println("3. Delete Client");
        out.println("4. Get All Clients");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
