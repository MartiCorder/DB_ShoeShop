package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.BufferedReader;
import java.io.PrintStream;
import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;
public class ShoeStoreManager {


    private final PrintStream out;
    private final BufferedReader in;
    private final ShoeStoreRepository shoestoreRepository;
    private final ModelFactory modelFactory;


    public ShoeStoreManager(BufferedReader in, PrintStream out, ShoeStoreRepository shoestoreRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.shoestoreRepository = shoestoreRepository;
        this.modelFactory = modelFactory;

    }
    public void start(){
        out.println("Shoe Store: ");

        var command = "";
        do {
            showShoeStoreMenu();
            command = readLine(in);

            switch (command){
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        }
        while (!command.equals("exit"));

        out.println("Exiting shoe store");
    }

    private void getAll() {
        var asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Name", "Owner", "Location", "ID Inventory");
        asciiTable.addRule();

        for (var shoeStore : shoestoreRepository.getAll()) {
            asciiTable.addRow(shoeStore.getId(), shoeStore.getName(), shoeStore.getOwner(), shoeStore.getLocation(), shoeStore.getInventoryId());
            asciiTable.addRule();
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);

        String render = asciiTable.render();
        System.out.println(render);
    }

    private void delete() {
        var shoestore = modelFactory.createShoeStore();

        out.println("Enter the ID of the Shoe Store to delete:");
        int id = Integer.parseInt(readLine(in));
        shoestore.setId(id);

        shoestoreRepository.delete(shoestore);
        out.println("Successfully deleted");
    }

    private void update() {
        try {
            var shoeStore = modelFactory.createShoeStore();

            out.println("Enter the ID of the Shoe Store to update:");
            int id = Integer.parseInt(readLine(in));
            shoeStore.setId(id);

            out.println("Name");
            shoeStore.setName(readLine(in));

            out.println("Owner");
            shoeStore.setOwner(readLine(in));

            out.println("Location");
            shoeStore.setLocation(readLine(in));

            out.println("ID Inventory");
            shoeStore.setInventoryId(Integer.parseInt(readLine(in)));

            shoestoreRepository.save(shoeStore);
            out.println("Successfully updated");
        } catch (NumberFormatException e) {
            out.println("Invalid Shoe Store ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the Shoe Store: " + e.getMessage());
        }
    }

    private void insert() {

        var shoeStore = modelFactory.createShoeStore();

        out.println("Store ID");
        shoeStore.setId(Integer.parseInt(readLine(in)));

        out.println("Name");
        shoeStore.setName(readLine(in));

        out.println("Owner");
        shoeStore.setOwner(readLine(in));

        out.println("Location");
        shoeStore.setLocation(readLine(in));

        out.println("ID Inventory");
        shoeStore.setInventoryId(Integer.parseInt(readLine(in)));

        shoestoreRepository.save(shoeStore);
        out.println("Successfully inserted");
    }

    private void showShoeStoreMenu() {
        out.println("1. Insert");
        out.println("2. Update");
        out.println("3. Delete");
        out.println("4. GetAll");
    }
}
