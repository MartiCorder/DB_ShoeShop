package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.readLine;

public class ShoeManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ShoeRepository shoeRepository;
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;

    public ShoeManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.repositoryFactory = repositoryFactory;
        this.shoeRepository = repositoryFactory.getShoeRepository();
        this.modelFactory = modelFactory;
    }

    public void start(){
        out.println("\n*** Shoe Management ***\n");

        var command = "";
        do {
            showShoeMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> deleteShoe();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Shoe Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Shoes ***\n");

        Set<Shoe> shoeSet = shoeRepository.getAll();

        List<Shoe> shoes = shoeSet.stream()
                .sorted(Comparator.comparingInt(Shoe::getId))
                .collect(Collectors.toList());

        out.println(AsciiTable.getTable(shoes, Arrays.asList(
                new Column().header("Id").with(shoe -> String.valueOf(shoe.getId())),
                new Column().header("Model ID").with(shoe -> String.valueOf(shoe.getModels().getId())),
                new Column().header("Inventory ID").with(shoe -> {
                    Inventory inventory = shoe.getInventories();
                    return inventory != null ? String.valueOf(inventory.getId()) : "N/A";
                }),
                new Column().header("Price").with(shoe -> String.valueOf(shoe.getPrice())),
                new Column().header("Color").with(Shoe::getColor),
                new Column().header("Size").with(Shoe::getSize)
        )));
    }
    private void deleteShoe(){
        out.println("\n*** Delete Shoe ***\n");

        var shoe = modelFactory.createShoe();

        out.println("Enter the ID of the shoe to delete:");
        int id = Integer.parseInt(readLine(in));
        shoe.setId(id);

        if (shoeRepository.get(id) != null) {
            shoeRepository.delete(shoe);
            out.println("\nSuccessfully deleted.\n");
        } else {
            out.println("\nThe shoe with ID " + id + " does not exist.\n");
        }
    }

    private void update() {
        out.println("\n*** Update Shoe ***\n");

        try {
            var shoe = modelFactory.createShoe();

            out.println("Enter the ID of the shoe to update:");
            int id = Integer.parseInt(readLine(in));
            shoe.setId(id);

            out.println("Enter new Model ID:");
            int modelId = Integer.parseInt(readLine(in));
            Model model = repositoryFactory.getModelRepository().get(modelId);
            if (model == null) {
                out.println("Error: Model ID " + modelId + " does not exist.");
                return;
            }
            shoe.setModels(model);

            out.println("Enter new Inventory ID:");
            int inventoryId = Integer.parseInt(readLine(in));
            Inventory inventory = repositoryFactory.getInventoryRepository().get(inventoryId);
            if (inventory == null) {
                out.println("Error: Inventory ID " + inventoryId + " does not exist.");
                return;
            }
            shoe.setInventories(inventory);

            out.println("Enter new Price:");
            shoe.setPrice(Double.parseDouble(readLine(in)));

            out.println("Enter new Color:");
            shoe.setColor(readLine(in));

            out.println("Enter new Size:");
            shoe.setSize(readLine(in));

            shoeRepository.save(shoe);

            out.println("\nSuccessfully updated.\n");
        } catch (NumberFormatException e) {
            out.println("Invalid input. Please enter valid numbers for IDs and Price.");
        } catch (Exception e) {
            out.println("An error occurred: " + e.getMessage());
        }
    }


    private void insert(){
        out.println("\n*** Insert Shoe ***\n");

        var shoe = modelFactory.createShoe();

        try {
            out.println("Enter the Model ID:");
            int modelId = Integer.parseInt(readLine(in));
            Model model = repositoryFactory.getModelRepository().get(modelId);
            if (model == null) {
                out.println("Error: Model ID " + modelId + " does not exist.");
                return;
            }
            shoe.setModels(model);

            out.println("Enter the Inventory ID:");
            int inventoryId = Integer.parseInt(readLine(in));
            Inventory inventory = repositoryFactory.getInventoryRepository().get(inventoryId);
            if (inventory == null) {
                out.println("Error: Inventory ID " + inventoryId + " does not exist.");
                return;
            }
            shoe.setInventories(inventory);

            out.println("Enter the Price:");
            shoe.setPrice(Double.parseDouble(readLine(in)));

            out.println("Enter the Color:");
            shoe.setColor(readLine(in));

            out.println("Enter the Size:");
            shoe.setSize(readLine(in));

            shoeRepository.save(shoe);

            out.println("\nSuccessfully inserted.\n");
        } catch (NumberFormatException e) {
            out.println("Invalid input. Please enter valid numbers for IDs and Price.");
        } catch (Exception e) {
            out.println("An error occurred: " + e.getMessage());
        }
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
