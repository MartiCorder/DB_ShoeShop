package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ModelDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.InventoryDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ShoeManager {

    private final RestClient restClient;
    private final BufferedReader in;
    private final PrintStream out;

    public ShoeManager(RestClient restClient) {
        this.restClient = restClient;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out);
    }

    public void start() throws IOException, RequestException, JsonProcessingException {
        String command;
        do {
            showShoeMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> listAllShoes();
                case "2" -> showShoeDetails();
                case "3" -> addNewShoe();
                case "4" -> deleteShoeById();
                default -> out.println("Commanda no vàlida.");
            }
        } while (!command.equals("exit"));
    }

    private void listAllShoes() throws RequestException {
        out.println("\n*** Llista de Sabates ***\n");

        var shoes = restClient.getAll("shoe", ShoeDto[].class);

        for (ShoeDto shoe : shoes) {
            out.println("ID: " + shoe.getId());
        }
    }

    private void showShoeDetails() throws RequestException {
        out.print("ID de la sabata a mostrar: ");
        var shoeId = readLine(in);

        try {
            var shoe = restClient.get("shoe/" + shoeId, ShoeDto.class);
            if (shoe != null) {
                out.println("Detalls de la sabata:");
                out.println("ID: " + shoe.getId());
                out.println("Preu: " + shoe.getPrice());
                out.println("Color: " + shoe.getColor());
                out.println("Mida: " + shoe.getSize());

                ModelDto model = (ModelDto) shoe.getModels();
                if (model != null) {
                    out.println("Model ID: " + model.getId());
                    out.println("Nom del Model: " + model.getName());
                } else {
                    out.println("Model: No disponible");
                }


                InventoryDto inventory = (InventoryDto) shoe.getInventories();
                if (inventory != null) {
                    out.println("Inventari ID: " + inventory.getId());
                    out.println("Quantitat disponible: " + inventory.getCapacity());
                } else {
                    out.println("Inventari: No disponible");
                }
            } else {
                out.println("No s'ha trobat cap sabata amb ID " + shoeId);
            }
        } catch (RequestException e) {
            out.println("Error al obtenir la sabata: " + e.getMessage());
        }
    }

    private void addNewShoe() throws IOException, JsonProcessingException, RequestException {
        var shoe = new ShoeDto();

        out.print("Introdueix el preu de la sabata: ");
        shoe.setPrice(Double.parseDouble(readLine(in)));

        out.print("Introdueix el color de la sabata: ");
        shoe.setColor(readLine(in));

        out.print("Introdueix la mida de la sabata: ");
        shoe.setSize(readLine(in));

        // Afegeix el model
        out.print("Introdueix l'ID del model: ");
        var model = new ModelDto();
        model.setId(Integer.parseInt(readLine(in)));
        shoe.setModels(model);

        // Afegeix l'inventari
        out.print("Introdueix l'ID d'inventari: ");
        var inventory = new InventoryDto();
        inventory.setId(Integer.parseInt(readLine(in)));
        shoe.setInventories(inventory);

        try {
            restClient.post("shoe", Mappers.get().writeValueAsString(shoe));
            out.println("Sabata afegida correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al afegir la sabata: " + e.getMessage());
        }
    }

    private void deleteShoeById() throws RequestException {
        out.print("Introdueix l'ID de la sabata a eliminar: ");
        var shoeId = readLine(in);

        try {
            var shoe = restClient.get("shoe/" + shoeId, ShoeDto.class);

            if (shoe != null) {
                restClient.delete("shoes", shoeId);
                out.println("Sabata eliminada correctament.");
            } else {
                out.println("No s'ha trobat cap sabata amb ID " + shoeId);
            }
        } catch (RequestException e) {
            out.println("Error al eliminar la sabata: " + e.getMessage());
        }
    }

    private void showShoeMenu() {
        out.println("\n--- Menu de Gestió de Sabates ---");
        out.println("1. Llista de totes les sabates");
        out.println("2. Detalls d'una sabata");
        out.println("3. Afegir una nova sabata");
        out.println("4. Eliminar una sabata existent");
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
