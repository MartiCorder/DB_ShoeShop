package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;

public class ShoeStoreManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ShoeStoreRepository shoeStoreRepository;
    private final ModelFactory modelFactory;

    public ShoeStoreManager(BufferedReader in, PrintStream out, ShoeStoreRepository shoeStoreRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.shoeStoreRepository = shoeStoreRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        out.println("\n*** Shoe Store Management ***\n");

        var command = "";
        do {
            showShoeStoreMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Shoe Store Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Shoe Store ***\n");
        var shoestores = shoeStoreRepository.getAll();

        out.println(AsciiTable.getTable(shoestores, Arrays.asList(
                new Column().header("Id").with(shoestore -> String.valueOf(shoestore.getId())),
                new Column().header("Name").with(ShoeStore::getName),
                new Column().header("Owner").with(ShoeStore::getOwner),
                new Column().header("Location").with(ShoeStore::getLocation),
                new Column().header("Inventory ID").with(shoestore -> String.valueOf(shoestore.getInventoryId()))
        )));
    }

    private void delete() {
        out.println("\n*** Delete Shoe Store ***\n");

        var shoeStore = modelFactory.createShoeStore();

        out.println("Enter the ID of the Shoe Store to delete:");
        int id = Integer.parseInt(readLine(in));
        shoeStore.setId(id);

        shoeStoreRepository.delete(shoeStore);

        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Shoe Store ***\n");

        try {
            var shoeStore = modelFactory.createShoeStore();

            out.println("Enter the ID of the Shoe Store to update:");
            int id = Integer.parseInt(readLine(in));
            shoeStore.setId(id);

            out.println("Enter new Name:");
            shoeStore.setName(readLine(in));

            out.println("Enter new Owner:");
            shoeStore.setOwner(readLine(in));

            out.println("Enter new Location:");
            shoeStore.setLocation(readLine(in));

            out.println("Enter new Inventory ID:");
            shoeStore.setInventoryId(Integer.parseInt(readLine(in)));

            shoeStoreRepository.save(shoeStore);

            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid Shoe Store ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the Shoe Store: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Shoe Store ***\n");

        var shoeStore = modelFactory.createShoeStore();

        out.println("Enter the Store ID:");
        shoeStore.setId(Integer.parseInt(readLine(in)));

        out.println("Enter the Name:");
        shoeStore.setName(readLine(in));

        out.println("Enter the Owner:");
        shoeStore.setOwner(readLine(in));

        out.println("Enter the Location:");
        shoeStore.setLocation(readLine(in));

        out.println("Enter the Inventory ID:");
        shoeStore.setInventoryId(Integer.parseInt(readLine(in)));

        shoeStoreRepository.save(shoeStore);

        out.println("\nSuccessfully inserted.\n");
    }

    private void showShoeStoreMenu() {
        out.println("\n*** Shoe Store Management Menu ***\n");
        out.println("1. Insert Shoe Store");
        out.println("2. Update Shoe Store");
        out.println("3. Delete Shoe Store");
        out.println("4. Get All Shoe Stores");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
