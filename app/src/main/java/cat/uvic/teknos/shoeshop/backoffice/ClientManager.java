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
import java.util.Arrays;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.readLine;

public class ClientManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ClientRepository clientRepository;
    private final ModelFactory modelFactory;

    private final RepositoryFactory repositoryFactory;

    public ClientManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory,
                          ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.repositoryFactory = repositoryFactory;
        this.clientRepository = repositoryFactory.getClientRepository();
        this.modelFactory = modelFactory;
    }

    public void start(){
        out.println("\n*** Client Management ***\n");

        var command = "";

        do {
            showClientMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insertClient();
                case "2" -> updateClient();
                case "3" -> deleteClient();
                case "4" -> getAllClients();
                case "5" -> insertAddress();
                case "6" -> insertShoeStore();
            }
        } while (!command.equals("exit"));

        out.println("\n*** Exiting Client Management ***\n");
    }

    private void getAllClients() {
        out.println("\n List of Clients \n");

        var clients = clientRepository.getAll();

        out.println(AsciiTable.getTable(clients, Arrays.asList(
                new Column().header("Id").with(client -> String.valueOf(client.getId())),
                new Column().header("DNI").with(Client::getDni),
                new Column().header("Name").with(Client::getName),
                new Column().header("Phone").with(Client::getPhone)
        )));

    }
    private void deleteClient() {
        out.println("\n*** Delete Client ***\n");

        out.println("Enter the ID of the client to delete:");

        int id = Integer.parseInt(readLine(in));
        Client client = clientRepository.get(id);
        if (client != null) {
            clientRepository.delete(client);
            out.println("Client deleted successfully.");
        } else {
            out.println("Client with ID " + id + " not found.");
        }
    }

    private void updateClient() {
        out.println("\n*** Update Client ***\n");
        out.println("Enter the ID of the client to update:");
        int id = Integer.parseInt(readLine(in));
        Client client = clientRepository.get(id);
        if (client != null) {
            out.println("Enter new DNI:");
            String dni =readLine(in);
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
    }

    private void insertClient() {
        out.println("\n*** Insert Client ***\n");

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
        ShoeStore shoeStore = insertShoeStore();

        client.setAddresses(address);
        client.setShoeStores(shoeStore);

        clientRepository.save(client);
        out.println("Client inserted successfully.");

    }
    private Address insertAddress(){
        out.println("\n*** Insert Address ***\n");
        Address address = modelFactory.createAddress();
        out.println("Enter the Location:");
        address.setLocation(readLine(in));
        out.println("Address inserted successfully.");
        return address;
    }

    private ShoeStore insertShoeStore(){
        out.println("\n*** Insert Shoe Store ***\n");
        ShoeStore shoeStore = modelFactory.createShoeStore();
        out.println("Enter the Name:");
        shoeStore.setName(readLine(in));
        out.println("Enter the Owner:");
        shoeStore.setOwner(readLine(in));
        out.println("Enter the Location:");
        shoeStore.setLocation(readLine(in));
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

