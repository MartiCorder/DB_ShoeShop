package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.AddressDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ClientDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeStoreDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class ClientManager {

    private final RestClient restClient;
    private final BufferedReader in;
    private final PrintStream out;

    public ClientManager(RestClient restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
        this.out = new PrintStream(System.out);
    }

    public void start() throws RequestException, JsonProcessingException {
        String command;
        do {
            showClientMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> listAllClients();
                case "2" -> showClientDetails();
                case "3" -> addNewClient();
                case "4" -> deleteClient();
                case "5" -> updateClient();
                default -> out.println("Commanda no vàlida.");
            }

        } while (!command.equals("exit"));
    }

    private void listAllClients() throws RequestException {
        var clients = restClient.getAll("clients", ClientDto[].class);
        showClientsTable(clients);
    }

    private void showClientDetails() throws RequestException {
        out.print("ID del client a mostrar: ");
        var clientId = readLine(in);

        try {
            var client = restClient.get("clients/" + clientId, ClientDto.class);

            if (client != null) {
                out.println("Detalls del client: ");
                out.println("ID: " + client.getId());
                out.println("Nom: " + client.getName());
                out.println("DNI: " + client.getDni());
                out.println("Número de telèfon: " + client.getPhone());

                AddressDto address = (AddressDto) client.getAddresses();
                if (address != null) {
                    out.println("Adreça: " + address.getLocation());
                } else {
                    out.println("Adreça: No disponible");
                }

                ShoeStoreDto shoeStore = (ShoeStoreDto) client.getShoeStores();
                if (shoeStore != null) {
                    out.println("Botiga de sabates: " + shoeStore.getName());
                } else {
                    out.println("Botiga de sabates: No disponible");
                }

            } else {
                out.println("El client amb ID " + clientId + " no existeix.");
            }
        } catch (RequestException e) {
            out.println("Error al obtenir el client: " + e.getMessage());
        }
    }

    private void addNewClient() throws RequestException, JsonProcessingException {
        out.print("Insereix el nom del client a afegir: ");
        var client = new ClientDto();
        client.setName(readLine(in));
        out.println("Insereix el DNI:");
        client.setDni(readLine(in));
        out.println("Insereix el número de telèfon:");
        client.setPhone(readLine(in));

        out.print("Insereix l'adreça: ");
        var address = new AddressDto();
        address.setLocation(readLine(in));
        client.setAddresses(address);

        out.print("Insereix l'id de la botiga: ");
        var shoeStore = new ShoeStoreDto();
        shoeStore.setId(Integer.parseInt(readLine(in)));
        client.setShoeStores(shoeStore);

        try {
            restClient.post("clients/", Mappers.get().writeValueAsString(client));
            out.println("Client afegit correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al afegir el client: " + e.getMessage());
        }
    }

    private void deleteClient() throws RequestException {
        out.print("ID del client a eliminar: ");
        var clientId = readLine(in);

        try {

            var client = restClient.get("clients/" + clientId, ClientDto.class);

            if (client != null) {
                restClient.delete("clients/" + clientId, null);
                out.println("Client eliminat correctament.");
            } else {
                out.println("No s'ha trobat cap client amb ID " + clientId);
            }
        } catch (RequestException e) {
            out.println("Error al eliminar el client: " + e.getMessage());
        }
    }
    private void updateClient() throws RequestException, JsonProcessingException {
        out.print("ID del client a editar: ");
        var clientId = readLine(in);

        var existingClient = restClient.get("clients/" + clientId, ClientDto.class);
        if (existingClient == null) {
            System.out.println("El client amb ID " + clientId + " no existeix.");
            return;
        }

        var client = new ClientDto();
        out.print("Insereix el nom del client: ");
        client.setName(readLine(in));
        out.println("Insereix el DNI:");
        client.setDni(readLine(in));
        out.println("Insereix el número de telèfon:");
        client.setPhone(readLine(in));

        var address = new AddressDto();
        out.print("Insereix l'adreça: ");
        address.setLocation(readLine(in));
        client.setAddresses(address);

        var shoeStore = new ShoeStoreDto();
        out.print("Insereix l'id de la botiga: ");
        shoeStore.setId(Integer.parseInt(readLine(in)));
        client.setShoeStores(shoeStore);

        try {
            restClient.put("clients/" + clientId, Mappers.get().writeValueAsString(client));
            out.println("Client actualitzat correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al actualitzar el client: " + e.getMessage());
        }
    }

    private void showClientsTable(ClientDto[] clients) {
        String table = AsciiTable.getTable(Arrays.asList(clients), Arrays.asList(
                new Column().header("ID").with(client -> String.valueOf(client.getId())),
                new Column().header("Nom").with(ClientDto::getName),
                new Column().header("DNI").with(ClientDto::getDni),
                new Column().header("Telèfon").with(ClientDto::getPhone),
                new Column().header("Adreça").with(client -> client.getAddresses() != null ? client.getAddresses().getLocation() : "N/A"),
                new Column().header("Botiga ID").with(client -> client.getShoeStores() != null ? String.valueOf(client.getShoeStores().getId()) : "N/A")
        ));
        out.println(table);
    }

    private String readLine(BufferedReader in) {
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al llegir l'opció del menú: " + e);
        }
        return command;
    }

    private void showClientMenu() {
        out.println("\n--- Menu de Gestió de Clients ---");
        out.println("1. Llista de tots els clients");
        out.println("2. Detalls d'un client");
        out.println("3. Afegir un nou client");
        out.println("4. Eliminar un client existent");
        out.println("5. Editar un client existent");
        out.println("Escriu 'exit' per tornar al menú principal.");
    }
} 
