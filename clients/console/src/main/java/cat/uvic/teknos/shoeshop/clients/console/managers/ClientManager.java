package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.AddressDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ClientDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeStoreDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientManager {

    private final RestClient restClient;
    private final BufferedReader in;

    public ClientManager(RestClient restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
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
                default -> System.out.println("Commanda no vàlida.");
            }

        } while (!command.equals("exit"));
    }

    private void listAllClients() throws RequestException {
        var clients = restClient.getAll("client", ClientDto[].class);
        System.out.println("Llista de clients:");
        for (ClientDto client : clients) {
            System.out.println(client.getName() + ", ID: " + client.getId());
        }
    }

    private void showClientDetails() throws RequestException {
        System.out.print("ID del client a mostrar: ");
        var clientId = readLine(in);

        try {
            var client = restClient.get("client/" + clientId, ClientDto.class);

            if (client != null) {
                System.out.println("Detalls del client: ");
                System.out.println("ID: " + client.getId());
                System.out.println("Nom: " + client.getName());
                System.out.println("DNI: " + client.getDni());
                System.out.println("Número de telèfon: " + client.getPhone());

                AddressDto address = (AddressDto) client.getAddresses();
                if (address != null) {
                    System.out.println("Adreça: " + address.getLocation());
                } else {
                    System.out.println("Adreça: No disponible");
                }

                ShoeStoreDto shoeStore = (ShoeStoreDto) client.getShoeStores();
                if (shoeStore != null) {
                    System.out.println("Botiga de sabates: " + shoeStore.getName());
                } else {
                    System.out.println("Botiga de sabates: No disponible");
                }

            } else {
                System.out.println("El client amb ID " + clientId + " no existeix.");
            }
        } catch (RequestException e) {
            System.out.println("Error al obtenir el client: " + e.getMessage());
        }
    }

    private void addNewClient() throws RequestException, JsonProcessingException {
        System.out.print("Insereix el nom del client a afegir: ");
        var client = new ClientDto();
        client.setName(readLine(in));
        System.out.println("Insereix el DNI:");
        client.setDni(readLine(in));
        System.out.println("Insereix el número de telèfon:");
        client.setPhone(readLine(in));
        System.out.print("Insereix l'adreça: ");

        var address = new AddressDto();
        address.setLocation(readLine(in));
        client.setAddresses(address);

        System.out.print("Insereix l'id de la botiga: ");
        var shoeStore = new ShoeStoreDto();
        shoeStore.setId(Integer.parseInt(readLine(in)));
        client.setShoeStores(shoeStore);

        try {
            restClient.post("client/", Mappers.get().writeValueAsString(client));
            System.out.println("Client afegit correctament.");
        } catch (JsonProcessingException | RequestException e) {
            System.out.println("Error al afegir el client: " + e.getMessage());
        }
    }

    private void deleteClient() throws RequestException {
        System.out.print("ID del client a eliminar: ");
        var clientId = readLine(in);

        try {
            var client = restClient.get("client/" + clientId, ClientDto.class);

            if (client != null) {
                restClient.delete("client", clientId);
                System.out.println("Client eliminat correctament.");
            } else {
                System.out.println("El client amb ID " + clientId + " no existeix.");
            }
        } catch (RequestException e) {
            System.out.println("Error al eliminar el client: " + e.getMessage());
        }
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
        System.out.println("\n--- Menu de Gestió de Clients ---");
        System.out.println("1. Llista de tots els clients");
        System.out.println("2. Detalls d'un client");
        System.out.println("3. Afegir un nou client");
        System.out.println("4. Eliminar un client existent");
        System.out.println("Escriu 'exit' per tornar al menú principal.");
    }
}
