package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeStoreDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ShoeStoreManager {

    private final RestClient restClient;
    private final BufferedReader in;
    private final PrintStream out;

    public ShoeStoreManager(RestClient restClient) {
        this.restClient = restClient;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out);
    }

    public void start() throws IOException, RequestException, JsonProcessingException {
        String command;
        do {
            showShoeStoreMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> listAllShoeStores();
                case "2" -> showShoeStoreDetails();
                case "3" -> addNewShoeStore();
                case "4" -> deleteShoeStoreById();
                default -> out.println("Commanda no vàlida.");
            }
        } while (!command.equals("exit"));
    }

    private void listAllShoeStores() throws RequestException {
        out.println("\n*** Llista de Botigues de Sabates ***\n");

        var shoeStores = restClient.getAll("/shoeStore", ShoeStoreDto[].class);

        for (ShoeStoreDto store : shoeStores) {
            out.println(store.getName() + ", ID: " + store.getId());
        }
    }

    private void showShoeStoreDetails() throws RequestException {
        out.print("ID de la botiga a mostrar: ");
        var storeId = readLine(in);

        try {
            var store = restClient.get("shoeStore/" + storeId, ShoeStoreDto.class);
            if (store != null) {
                out.println("Detalls de la botiga:");
                out.println("ID: " + store.getId());
                out.println("Nom: " + store.getName());
                out.println("Propietari: " + store.getOwner());
                out.println("Ubicació: " + store.getLocation());
                out.println("Inventaris: " + store.getInventories());
                out.println("Proveïdors: " + store.getSuppliers());
                out.println("Clients: " + store.getClients());
            } else {
                out.println("No s'ha trobat cap botiga amb ID " + storeId);
            }
        } catch (RequestException e) {
            out.println("Error al obtenir la botiga: " + e.getMessage());
        }
    }

    private void addNewShoeStore() throws IOException, JsonProcessingException, RequestException {
        var store = new ShoeStoreDto();

        out.print("Introdueix el nom de la botiga: ");
        store.setName(readLine(in));

        out.print("Introdueix el nom del propietari de la botiga: ");
        store.setOwner(readLine(in));

        out.print("Introdueix la ubicació de la botiga: ");
        store.setLocation(readLine(in));

        try {
            restClient.post("shoeStore", Mappers.get().writeValueAsString(store));
            out.println("Botiga afegida correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al afegir la botiga: " + e.getMessage());
        }
    }

    private void deleteShoeStoreById() throws RequestException {
        out.print("Introdueix l'ID de la botiga a eliminar: ");
        var storeId = readLine(in);

        try {
            var store = restClient.get("shoeStore/" + storeId, ShoeStoreDto.class);

            if (store != null) {
                restClient.delete("shoeStore/", storeId);
                out.println("Botiga eliminada correctament.");
            } else {
                out.println("No s'ha trobat cap botiga amb ID " + storeId);
            }
        } catch (RequestException e) {
            out.println("Error al eliminar la botiga: " + e.getMessage());
        }
    }

    private void showShoeStoreMenu() {
        out.println("\n--- Menu de Gestió de Botigues de Sabates ---");
        out.println("1. Llista de totes les botigues");
        out.println("2. Detalls d'una botiga");
        out.println("3. Afegir una nova botiga");
        out.println("4. Eliminar una botiga existent");
        out.println("Escriu 'exit' per tornar al menú principal.");
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
}
