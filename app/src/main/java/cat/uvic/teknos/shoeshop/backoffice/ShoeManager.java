package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.BufferedReader;
import java.io.PrintStream;
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
    public void start(){
        out.println("Shoe: ");

        var command = "";
        do {
            showShoeMenu();
            command = readLine(in);

            switch (command){
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        }
        while (!command.equals("exit"));

        out.println("Exiting shoe");
    }

    private void getAll() {
        var asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "ID Model", "ID Inventory", "Price", "Color", "Size");
        asciiTable.addRule();

        for (var shoe : shoeRepository.getAll()) {
            asciiTable.addRow(shoe.getId(), shoe.getModelId(), shoe.getInventoryId(), shoe.getPrice(), shoe.getColor(), shoe.getSize());
            asciiTable.addRule();
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);

        String render = asciiTable.render();
        System.out.println(render);
    }

    private void delete() {
        var shoe = modelFactory.createShoe();

        out.println("Enter the ID of the shoe to delete:");
        int id = Integer.parseInt(readLine(in));
        shoe.setId(id);

        shoeRepository.delete(shoe);
        out.println("Successfully deleted");
    }

    private void update() {
        try {
            var shoe = modelFactory.createShoe();

            out.println("Enter the ID of the shoe to update:");
            int id = Integer.parseInt(readLine(in));
            shoe.setId(id);

            out.println("Model");
            shoe.setModelId(Integer.parseInt(readLine(in)));

            out.println("Inventory ID");
            shoe.setInventoryId(Integer.parseInt(readLine(in)));

            out.println("Price");
            shoe.setPrice(Integer.parseInt(readLine(in)));

            out.println("Color");
            shoe.setColor(readLine(in));

            out.println("Size");
            shoe.setSize(readLine(in));

            shoeRepository.save(shoe);
            out.println("Successfully updated");
        } catch (NumberFormatException e) {
            out.println("Invalid shoe ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the shoe: " + e.getMessage());
        }
    }

    private void insert() {

        var shoe = modelFactory.createShoe();

        out.println("Model");
        shoe.setModelId(Integer.parseInt(readLine(in)));

        out.println("Inventory ID");
        shoe.setInventoryId(Integer.parseInt(readLine(in)));

        out.println("Price");
        shoe.setPrice(Integer.parseInt(readLine(in)));

        out.println("Color");
        shoe.setColor(readLine(in));

        out.println("Size");
        shoe.setSize(readLine(in));

        shoeRepository.save(shoe);
        out.println("Successfully inserted");
    }

    private void showShoeMenu() {
        out.println("1. Insert");
        out.println("2. Update");
        out.println("3. Delete");
        out.println("4. GetAll");
    }
}
