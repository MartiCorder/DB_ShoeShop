package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.ShoeDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.ModelDto;
import cat.uvic.teknos.shoeshop.clients.console.dto.InventoryDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

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
                case "5" -> updateShoe();
                default -> out.println("Commanda no vàlida.");
            }
        } while (!command.equals("exit"));
    }

    private void listAllShoes() throws RequestException {
        var shoes = restClient.getAll("shoes", ShoeDto[].class);
        showShoesTable(shoes);
    }

    private void showShoeDetails() throws RequestException {
        out.print("ID de la sabata a mostrar: ");
        var shoeId = readLine(in);

        try {
            var shoe = restClient.get("shoes/" + shoeId, ShoeDto.class);
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

        out.print("Introdueix l'ID del model: ");
        var model = new ModelDto();
        model.setId(Integer.parseInt(readLine(in)));
        shoe.setModels(model);

        out.print("Introdueix l'ID d'inventari: ");
        var inventory = new InventoryDto();
        inventory.setId(Integer.parseInt(readLine(in)));
        shoe.setInventories(inventory);

        try {
            restClient.post("shoes", Mappers.get().writeValueAsString(shoe));
            out.println("Sabata afegida correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al afegir la sabata: " + e.getMessage());
        }
    }

    private void deleteShoeById() throws RequestException {
        out.print("Introdueix l'ID de la sabata a eliminar: ");
        var shoeId = readLine(in);

        try {
            var shoe = restClient.get("shoes/" + shoeId, ShoeDto.class);

            if (shoe != null) {
                restClient.delete("shoes/"+ shoeId, null);
                out.println("Sabata eliminada correctament.");
            } else {
                out.println("No s'ha trobat cap sabata amb ID " + shoeId);
            }
        } catch (RequestException e) {
            out.println("Error al eliminar la sabata: " + e.getMessage());
        }
    }

    private void updateShoe() throws IOException, JsonProcessingException, RequestException {
        out.print("Introdueix l'ID de la sabata a editar: ");
        var shoeId = readLine(in);

        var existingShoe = restClient.get("shoes/" + shoeId, ShoeDto.class);
        if (existingShoe == null) {
            out.println("La sabata amb ID " + shoeId + " no existeix.");
            return;
        }

        var shoe = new ShoeDto();
        out.print("Introdueix el preu de la sabata: ");
        shoe.setPrice(Double.parseDouble(readLine(in)));

        out.print("Introdueix el color de la sabata: ");
        shoe.setColor(readLine(in));

        out.print("Introdueix la mida de la sabata: ");
        shoe.setSize(readLine(in));

        var model = new ModelDto();
        out.print("Introdueix l'ID del model: ");
        model.setId(Integer.parseInt(readLine(in)));
        shoe.setModels(model);

        var inventory = new InventoryDto();
        out.print("Introdueix l'ID d'inventari: ");
        inventory.setId(Integer.parseInt(readLine(in)));
        shoe.setInventories(inventory);

        try {
            restClient.put("shoes/" + shoeId, Mappers.get().writeValueAsString(shoe));
            out.println("Sabata actualitzada correctament.");
        } catch (JsonProcessingException | RequestException e) {
            out.println("Error al actualitzar la sabata: " + e.getMessage());
        }
    }

    private void showShoesTable(ShoeDto[] shoes) {
        String table = AsciiTable.getTable(Arrays.asList(shoes), Arrays.asList(
                new Column().header("ID").with(shoe -> String.valueOf(shoe.getId())),
                new Column().header("Preu").with(shoe -> String.valueOf(shoe.getPrice())),
                new Column().header("Color").with(ShoeDto::getColor),
                new Column().header("Mida").with(ShoeDto::getSize),
                new Column().header("Model ID").with(shoe -> shoe.getModels() != null ? String.valueOf(shoe.getModels().getId()) : "N/A"),
                new Column().header("Inventari ID").with(shoe -> shoe.getInventories() != null ? String.valueOf(shoe.getInventories().getId()) : "N/A")
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

    private void showShoeMenu() {
        out.println("\n--- Menu de Gestió de Sabates ---");
        out.println("1. Llista de totes les sabates");
        out.println("2. Detalls d'una sabata");
        out.println("3. Afegir una nova sabata");
        out.println("4. Eliminar una sabata existent");
        out.println("5. Editar una sabata existent");
        out.println("Escriu 'exit' per tornar al menú principal.");
    }
}
