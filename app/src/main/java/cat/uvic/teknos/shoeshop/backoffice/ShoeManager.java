package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;

public class ShoeManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ShoeRepository shoeRepository;
    private final ModelFactory modelFactory;

    public ShoeManager(BufferedReader in, PrintStream out, ShoeRepository shoeRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.shoeRepository = shoeRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        out.println("\n*** Shoe Management ***\n");

        var command = "";
        do {
            showShoeMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Shoe Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Shoes ***\n");

        var shoes = shoeRepository.getAll();

        out.println(AsciiTable.getTable(shoes, Arrays.asList(
                new Column().header("Id").with(shoe -> String.valueOf(shoe.getId())),
                new Column().header("Model ID").with(shoe -> String.valueOf(shoe.getModelId())),
                new Column().header("Inventory ID").with(shoe -> String.valueOf(shoe.getInventoryId())),
                new Column().header("Price").with(shoe -> String.valueOf(shoe.getPrice())),
                new Column().header("Color").with(Shoe::getColor),
                new Column().header("Size").with(Shoe::getSize)
        )));
    }

    private void delete() {
        out.println("\n*** Delete Shoe ***\n");

        var shoe = modelFactory.createShoe();

        out.println("Enter the ID of the shoe to delete:");
        int id = Integer.parseInt(readLine(in));
        shoe.setId(id);

        shoeRepository.delete(shoe);

        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Shoe ***\n");

        try {
            var shoe = modelFactory.createShoe();

            out.println("Enter the ID of the shoe to update:");
            int id = Integer.parseInt(readLine(in));
            shoe.setId(id);

            out.println("Enter new Model ID:");
            shoe.setModelId(Integer.parseInt(readLine(in)));

            out.println("Enter new Inventory ID:");
            shoe.setInventoryId(Integer.parseInt(readLine(in)));

            out.println("Enter new Price:");
            shoe.setPrice(Integer.parseInt(readLine(in)));

            out.println("Enter new Color:");
            shoe.setColor(readLine(in));

            out.println("Enter new Size:");
            shoe.setSize(readLine(in));

            shoeRepository.save(shoe);

            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid shoe ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the shoe: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Shoe ***\n");

        var shoe = modelFactory.createShoe();

        out.println("Enter the Model ID:");
        shoe.setModelId(Integer.parseInt(readLine(in)));

        out.println("Enter the Inventory ID:");
        shoe.setInventoryId(Integer.parseInt(readLine(in)));

        out.println("Enter the Price:");
        shoe.setPrice(Integer.parseInt(readLine(in)));

        out.println("Enter the Color:");
        shoe.setColor(readLine(in));

        out.println("Enter the Size:");
        shoe.setSize(readLine(in));

        shoeRepository.save(shoe);

        out.println("\nSuccessfully inserted.\n");
    }

    private void showShoeMenu() {
        out.println("\n*** Shoe Management Menu ***\n");
        out.println("1. Insert Shoe");
        out.println("2. Update Shoe");
        out.println("3. Delete Shoe");
        out.println("4. Get All Shoes");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
