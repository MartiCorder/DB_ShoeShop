package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.readLine;

public class ClientManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ClientRepository clientRepository;
    private final ModelFactory modelFactory;

    private final RepositoryFactory repositoryFactory;
    private final Properties properties = new Properties();

    public ClientManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory,
                         ModelFactory modelFactory) throws IOException {
        this.out = out;
        this.in = in;
        this.repositoryFactory = repositoryFactory;
        this.clientRepository = repositoryFactory.getClientRepository();
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start(){
        out.println("\n*** Client Management ***\n");

        String command;
        do {
            showClientMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insertClient();
                case "2" -> updateClient();
                case "3" -> deleteClient();
                case "4" -> getAllClients();
            }
        } while (!command.equals("exit"));

        out.println("\n*** Exiting Client Management ***\n");
    }

    private void getAllClients() {
        out.println("\n*** List of Clients ***\n");

        Set<Client> clientSet = clientRepository.getAll();
        List<Client> clients = clientSet.stream()
                .sorted(Comparator.comparingInt(Client::getId))
                .collect(Collectors.toList());

        out.println(AsciiTable.getTable(clients, Arrays.asList(
                new Column().header("Id").with(client -> String.valueOf(client.getId())),
                new Column().header("DNI").with(Client::getDni),
                new Column().header("Name").with(Client::getName),
                new Column().header("Phone").with(Client::getPhone)
        )));
    }

    private void deleteClient() {
        out.println("\n*** Delete Client ***\n");

        try {
            out.println("Enter the ID of the client to delete:");
            int id = Integer.parseInt(readLine(in));
            Client client = clientRepository.get(id);
            if (client != null) {
                clientRepository.delete(client);
                out.println("Client deleted successfully.");
            } else {
                out.println("Client with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            out.println("Invalid Client ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while deleting the client: " + e.getMessage());
        }
    }


    private void updateClient() {
        out.println("\n*** Update Client ***\n");

        try {
            out.println("Enter the ID of the client to update:");
            int id = Integer.parseInt(readLine(in));
            Client client = clientRepository.get(id);
            if (client != null) {
                out.println("Enter new DNI:");
                String dni = readLine(in);
                out.println("Enter new Name:");
                String name = readLine(in);
                out.println("Enter new Phone:");
                String phone = readLine(in);

                client.setDni(dni);
                client.setName(name);
                client.setPhone(phone);
                clientRepository.save(client);
                out.println("Client updated successfully.");
            } else {
                out.println("Client with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            out.println("Invalid Client ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the client: " + e.getMessage());
        }
    }

    private void insertClient() {
        out.println("\n*** Insert Client ***\n");

        try {
            Client client = modelFactory.createClient();
            out.println("Enter the DNI:");
            String dni = readLine(in);
            out.println("Enter the Name:");
            String name = readLine(in);
            out.println("Enter the Phone:");
            String phone = readLine(in);

            if (dni.isBlank() || name.isBlank() || phone.isBlank()) {
                out.println("Error: DNI, Name, and Phone cannot be empty.");
                return;
            }

            client.setDni(dni);
            client.setName(name);
            client.setPhone(phone);

            Address address = insertAddress();

            out.println("Do you want to insert a new Shoe Store or relate to an existing one? (new/existing)");
            String choice = readLine(in);

            ShoeStore shoeStore;
            if (choice.equalsIgnoreCase("existing")) {
                shoeStore = selectExistingShoeStore();
            } else {
                shoeStore = insertShoeStore();
            }

            client.setAddresses(address);
            client.setShoeStores(shoeStore);

            clientRepository.save(client);
            out.println("Client inserted successfully.");
        } catch (Exception e) {
            out.println("An error occurred while inserting the client: " + e.getMessage());
        }
    }

    private Address insertAddress(){
        out.println("\n*** Insert Address ***\n");

        try {
            Address address = modelFactory.createAddress();
            out.println("Enter the Location:");
            address.setLocation(readLine(in));
            out.println("Address inserted successfully.");
            return address;
        } catch (Exception e) {
            out.println("An error occurred while inserting the address: " + e.getMessage());
            return null;
        }
    }

    private ShoeStore insertShoeStore(){
        out.println("\n*** Insert Shoe Store ***\n");

        try {
            ShoeStore shoeStore = modelFactory.createShoeStore();
            out.println("Enter the Name:");
            shoeStore.setName(readLine(in));
            out.println("Enter the Owner:");
            shoeStore.setOwner(readLine(in));
            out.println("Enter the Location:");
            shoeStore.setLocation(readLine(in));
            out.println("Shoe Store inserted successfully.");
            return shoeStore;
        } catch (Exception e) {
            out.println("An error occurred while inserting the shoe store: " + e.getMessage());
            return null;
        }
    }

    private ShoeStore selectExistingShoeStore() {
        out.println("\n*** Select Existing Shoe Store ***\n");

        try {
            Set<ShoeStore> shoeStoreSet = repositoryFactory.getShoeStoreRepository().getAll();

            List<ShoeStore> shoeStores = shoeStoreSet.stream()
                    .sorted(Comparator.comparingInt(ShoeStore::getId))
                    .collect(Collectors.toList());

            out.println(AsciiTable.getTable(shoeStores, Arrays.asList(
                    new Column().header("Id").with(store -> String.valueOf(store.getId())),
                    new Column().header("Name").with(ShoeStore::getName),
                    new Column().header("Owner").with(ShoeStore::getOwner),
                    new Column().header("Location").with(ShoeStore::getLocation)
            )));

            out.println("Enter the ID of the Shoe Store to relate to the client:");
            int id = Integer.parseInt(readLine(in));
            ShoeStore shoeStore = repositoryFactory.getShoeStoreRepository().get(id);
            if (shoeStore == null) {
                out.println("Shoe Store with ID " + id + " not found.");
            }
            return shoeStore;
        } catch (NumberFormatException e) {
            out.println("Invalid Shoe Store ID. Please enter a valid integer ID.");
            return null;
        } catch (Exception e) {
            out.println("An error occurred while selecting the shoe store: " + e.getMessage());
            return null;
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
