package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;

public class ModelManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final ModelRepository modelRepository;
    private final ModelFactory modelFactory;

    public ModelManager(BufferedReader in, PrintStream out, ModelRepository modelRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.modelRepository = modelRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        out.println("\n*** Model Management ***\n");

        var command = "";
        do {
            showModelMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Model Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Models ***\n");

        var models = modelRepository.getAll();

        out.println(AsciiTable.getTable(models, Arrays.asList(
                new Column().header("Id").with(model -> String.valueOf(model.getId())),
                new Column().header("Name").with(Model::getName),
                new Column().header("Brand").with(Model::getBrand)
        )));
    }

    private void delete() {
        out.println("\n*** Delete Model ***\n");

        var model = modelFactory.createModel();

        out.println("Enter the ID of the model to delete:");
        int id = Integer.parseInt(readLine(in));
        model.setId(id);

        modelRepository.delete(model);

        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Model ***\n");

        try {
            var model = modelFactory.createModel();

            out.println("Enter the ID of the model to update:");
            int id = Integer.parseInt(readLine(in));
            model.setId(id);

            out.println("Enter new Name:");
            model.setName(readLine(in));

            out.println("Enter new Brand:");
            model.setBrand(readLine(in));

            modelRepository.save(model);

            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid model ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the model: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Model ***\n");

        var model = modelFactory.createModel();

        out.println("Enter the ID:");
        model.setId(Integer.parseInt(readLine(in)));

        out.println("Enter the Name:");
        model.setName(readLine(in));

        out.println("Enter the Brand:");
        model.setBrand(readLine(in));

        modelRepository.save(model);

        out.println("\nSuccessfully inserted.\n");
    }

    private void showModelMenu() {
        out.println("\n*** Model Management Menu ***\n");
        out.println("1. Insert Model");
        out.println("2. Update Model");
        out.println("3. Delete Model");
        out.println("4. Get All Models");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
