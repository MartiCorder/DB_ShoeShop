package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.BufferedReader;
import java.io.PrintStream;
import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;

public class SupplierManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final SupplierRepository supplierRepository;
    private final ModelFactory modelFactory;


    public SupplierManager(BufferedReader in, PrintStream out, SupplierRepository supplierRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.supplierRepository = supplierRepository;
        this.modelFactory = modelFactory;

    }
    public void start(){
        out.println("Supplier: ");

        var command = "";
        do {
            showSupplierMenu();
            command = readLine(in);

            switch (command){
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        }
        while (!command.equals("exit"));

        out.println("Exiting supplier");
    }

    private void getAll() {
        var asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Name", "Phone");
        asciiTable.addRule();

        for (var supplier : supplierRepository.getAll()) {
            asciiTable.addRow(supplier.getId(), supplier.getName(), supplier.getPhone());
            asciiTable.addRule();
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);

        String render = asciiTable.render();
        System.out.println(render);
    }

    private void delete() {
        var supplier = modelFactory.createSupplier();

        out.println("Enter the ID of the supplier to delete:");
        int id = Integer.parseInt(readLine(in));
        supplier.setId(id);

        supplierRepository.delete(supplier);
        out.println("Successfully deleted");
    }

    private void update() {
        try {
            var supplier = modelFactory.createSupplier();

            out.println("Enter the ID of the supplier to update:");
            int id = Integer.parseInt(readLine(in));
            supplier.setId(id);

            out.println("Name");
            supplier.setName(readLine(in));

            out.println("Phone");
            supplier.setPhone(readLine(in));

            supplierRepository.save(supplier);
            out.println("Successfully updated");
        } catch (NumberFormatException e) {
            out.println("Invalid supplier ID. Please enter a valid integer ID.");
        } catch (Exception e) {
            out.println("An error occurred while updating the supplier: " + e.getMessage());
        }
    }

    private void insert() {

        var supplier = modelFactory.createSupplier();

        out.println("Name");
        supplier.setName(readLine(in));

        out.println("Phone");
        supplier.setPhone(readLine(in));

        supplierRepository.save(supplier);
        out.println("Successfully inserted");
    }

    private void showSupplierMenu() {
        out.println("1. Insert");
        out.println("2. Update");
        out.println("3. Delete");
        out.println("4. GetAll");
    }
}
