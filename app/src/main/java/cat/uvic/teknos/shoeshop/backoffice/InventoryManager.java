package cat.uvic.teknos.shoeshop.backoffice;


import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.BufferedReader;
import java.io.PrintStream;
import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;
public class InventoryManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final InventoryRepository inventoryRepository;
    private final ModelFactory modelFactory;


    public InventoryManager(BufferedReader in, PrintStream out, InventoryRepository inventoryRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.inventoryRepository = inventoryRepository;
        this.modelFactory = modelFactory;

    }
    public void start(){

        out.println("Inventory: ");

        var command = "";
        do {
            showInventoryMenu();
            command = readLine(in);

            switch (command){
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        }
        while (!command.equals("exit"));

        out.println("Exiting inventory");
    }

    private void getAll() {
        var asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Capacity");
        asciiTable.addRule();

        for (var inventory : inventoryRepository.getAll()) {
            asciiTable.addRow(inventory.getId(), inventory.getCapacity());
            asciiTable.addRule();
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);

        String render = asciiTable.render();
        System.out.println(render);
    }

    private void delete() {

        var inventory = modelFactory.createInventory();

        out.println("Enter the ID of the inventory to delete:");
        int id = Integer.parseInt(readLine(in));
        inventory.setId(id);

        inventoryRepository.delete(inventory);
    }

    private void update() {
        try {
            var inventory = modelFactory.createInventory();

            out.println("Capacity");
            inventory.setCapacity(Integer.parseInt(readLine(in)));

            out.println("Successfully updated");
            inventoryRepository.save(inventory);
        } catch (NumberFormatException e) {
            out.println("Invalid inventory ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the inventory: " + e.getMessage());
        }
    }

    private void insert() {

        var inventory = modelFactory.createInventory();

        out.println("Capacity");
        inventory.setCapacity(Integer.parseInt(readLine(in)));

        inventoryRepository.save(inventory);
        out.println("Successfully inserted");

    }

    private void showInventoryMenu() {
        out.println("1. Insert");
        out.println("2. Update");
        out.println("3. Delete");
        out.println("4. GetAll");
    }
}
