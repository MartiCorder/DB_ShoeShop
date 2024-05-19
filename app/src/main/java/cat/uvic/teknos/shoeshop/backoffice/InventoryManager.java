package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

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

    public void start() {
        out.println("\n*** Inventory Management ***\n");

        var command = "";
        do {
            showInventoryMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Inventory Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Inventory ***\n");

        var inventories = inventoryRepository.getAll();

        out.println(AsciiTable.getTable(inventories, Arrays.asList(
                new Column().header("Id").with(inventory -> String.valueOf(inventory.getId())),
                new Column().header("Capacity").with(inventory -> String.valueOf(inventory.getCapacity()))
        )));
    }

    private void delete() {
        out.println("\n*** Delete Inventory ***\n");

        var inventory = modelFactory.createInventory();

        out.println("Enter the ID of the inventory to delete:");
        int id = Integer.parseInt(readLine(in));
        inventory.setId(id);

        inventoryRepository.delete(inventory);
        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Inventory ***\n");

        try {
            var inventory = modelFactory.createInventory();

            out.println("Enter the ID of the inventory to update:");
            int id = Integer.parseInt(readLine(in));
            inventory.setId(id);

            out.println("Enter new Capacity:");
            inventory.setCapacity(Integer.parseInt(readLine(in)));

            inventoryRepository.save(inventory);
            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid inventory ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the inventory: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Inventory ***\n");

        var inventory = modelFactory.createInventory();

        out.println("Enter the Capacity:");
        inventory.setCapacity(Integer.parseInt(readLine(in)));

        inventoryRepository.save(inventory);
        out.println("\nSuccessfully inserted.\n");
    }

    private void showInventoryMenu() {
        out.println("\n*** Inventory Management Menu ***\n");
        out.println("1. Insert Inventory");
        out.println("2. Update Inventory");
        out.println("3. Delete Inventory");
        out.println("4. Get All Inventories");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
