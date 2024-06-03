package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.readLine;

public class ShoeStoreManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ShoeStoreRepository shoeStoreRepository;
    private final ModelFactory modelFactory;
    private final Properties properties = new Properties();

    public ShoeStoreManager(BufferedReader in, PrintStream out, ShoeStoreRepository shoeStoreRepository, ModelFactory modelFactory) throws IOException {
        this.out = out;
        this.in = in;
        this.shoeStoreRepository = shoeStoreRepository;
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start(){
        out.println("\n*** Shoe Store Management ***\n");

        String command;
        do {
            showShoeStoreMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> deleteShoeStore();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Shoe Store Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Shoe Stores ***\n");

        Set<ShoeStore> shoeStoreSet = shoeStoreRepository.getAll();

        List<ShoeStore> shoeStores = shoeStoreSet.stream()
                .sorted(Comparator.comparingInt(ShoeStore::getId))
                .collect(Collectors.toList());

        out.println(AsciiTable.getTable(shoeStores, Arrays.asList(
                new Column().header("Id").with(shoeStore -> String.valueOf(shoeStore.getId())),
                new Column().header("Name").with(ShoeStore::getName),
                new Column().header("Owner").with(ShoeStore::getOwner),
                new Column().header("Location").with(ShoeStore::getLocation),
                new Column().header("Inventory ID").with(shoeStore -> String.valueOf(shoeStore.getInventories()))
        )));
    }

    private void deleteShoeStore(){
        out.println("\n*** Delete Shoe Store ***\n");

        try {
            out.println("Enter the ID of the Shoe Store to delete:");
            int id = Integer.parseInt(readLine(in));

            ShoeStore shoeStore = shoeStoreRepository.get(id);
            if (shoeStore != null) {
                shoeStoreRepository.delete(shoeStore);
                out.println("\nSuccessfully deleted.\n");
            } else {
                out.println("\nThe Shoe Store with ID " + id + " does not exist.\n");
            }
        } catch (NumberFormatException e) {
            out.println("\nInvalid Shoe Store ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while deleting the Shoe Store: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        out.println("\n*** Update Shoe Store ***\n");

        try {
            out.println("Enter the ID of the Shoe Store to update:");
            int id = Integer.parseInt(readLine(in));

            ShoeStore shoeStore = shoeStoreRepository.get(id);
            if (shoeStore != null) {
                out.println("Enter new Name:");
                String name = readLine(in);
                out.println("Enter new Owner:");
                String owner = readLine(in);
                out.println("Enter new Location:");
                String location = readLine(in);

                shoeStore.setName(name);
                shoeStore.setOwner(owner);
                shoeStore.setLocation(location);

                shoeStoreRepository.save(shoeStore);
                out.println("\nSuccessfully updated.\n");
            } else {
                out.println("\nThe Shoe Store with ID " + id + " does not exist.\n");
            }
        } catch (NumberFormatException e) {
            out.println("\nInvalid Shoe Store ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the Shoe Store: " + e.getMessage() + "\n");
        }
    }

    private void insert(){
        out.println("\n*** Insert Shoe Store ***\n");

        try {
            ShoeStore shoeStore = modelFactory.createShoeStore();

            out.println("Enter the Name:");
            shoeStore.setName(readLine(in));
            out.println("Enter the Owner:");
            shoeStore.setOwner(readLine(in));
            out.println("Enter the Location:");
            shoeStore.setLocation(readLine(in));

            shoeStoreRepository.save(shoeStore);

            out.println("\nSuccessfully inserted.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while inserting the Shoe Store: " + e.getMessage() + "\n");
        }
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
