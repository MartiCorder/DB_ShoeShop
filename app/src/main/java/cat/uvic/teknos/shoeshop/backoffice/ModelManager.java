package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.BufferedReader;
import java.io.PrintStream;
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
    public void start(){
        out.println("Model: ");

        var command = "";
        do {
            showModelMenu();
            command = readLine(in);

            switch (command){
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        }
        while (!command.equals("exit"));

        out.println("Exiting model");
    }

    private void getAll() {
        var asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Name", "Brand");
        asciiTable.addRule();

        for (var model : modelRepository.getAll()) {
            asciiTable.addRow(model.getId(), model.getName(), model.getBrand());
            asciiTable.addRule();
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);

        String render = asciiTable.render();
        System.out.println(render);
    }

    private void delete() {
        var model = modelFactory.createModel();

        out.println("Enter the ID of the model to delete:");
        int id = Integer.parseInt(readLine(in));
        model.setId(id);

        modelRepository.delete(model);
        out.println("Successfully deleted");

    }

    private void update() {
        try {
            var model = modelFactory.createModel();

            out.println("Name");
            model.setName(readLine(in));

            out.println("Brand");
            model.setBrand(readLine(in));

            modelRepository.save(model);
            out.println("Successfully updated");

        } catch (NumberFormatException e) {
            out.println("Invalid model ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the model: " + e.getMessage());
        }
    }

    private void insert() {

        var model = modelFactory.createModel();

        out.println("Model ID");
        model.setId(Integer.parseInt(readLine(in)));

        out.println("Name");
        model.setName(readLine(in));

        out.println("Brand");
        model.setBrand(readLine(in));

        modelRepository.save(model);
        out.println("Successfully inserted");
    }

    private void showModelMenu() {
        out.println("1. Insert");
        out.println("2. Update");
        out.println("3. Delete");
        out.println("4. GetAll");
    }
}
