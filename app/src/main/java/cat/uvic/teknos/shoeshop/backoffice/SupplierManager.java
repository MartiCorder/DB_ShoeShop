package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

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

    public void start() {
        out.println("\n*** Supplier Management ***\n");

        var command = "";
        do {
            showSupplierMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Supplier Management ***\n");
    }

    private void getAll() {
        out.println("\n*** List of Suppliers ***\n");
        var suppliers = supplierRepository.getAll();

        out.println(AsciiTable.getTable(suppliers, Arrays.asList(
                new Column().header("Id").with(supplier -> String.valueOf(supplier.getId())),
                new Column().header("Name").with(Supplier::getName),
                new Column().header("Phone").with(Supplier::getPhone)
        )));
    }

    private void delete() {
        out.println("\n*** Delete Supplier ***\n");

        var supplier = modelFactory.createSupplier();

        out.println("Enter the ID of the supplier to delete:");
        int id = Integer.parseInt(readLine(in));
        supplier.setId(id);

        supplierRepository.delete(supplier);

        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Supplier ***\n");

        try {
            var supplier = modelFactory.createSupplier();

            out.println("Enter the ID of the supplier to update:");
            int id = Integer.parseInt(readLine(in));
            supplier.setId(id);

            out.println("Enter new Name:");
            supplier.setName(readLine(in));

            out.println("Enter new Phone:");
            supplier.setPhone(readLine(in));

            supplierRepository.save(supplier);

            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid supplier ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the supplier: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Supplier ***\n");

        var supplier = modelFactory.createSupplier();

        out.println("Enter the Name:");
        supplier.setName(readLine(in));

        out.println("Enter the Phone:");
        supplier.setPhone(readLine(in));

        supplierRepository.save(supplier);

        out.println("\nSuccessfully inserted.\n");
    }

    private void showSupplierMenu() {
        out.println("\n*** Supplier Management Menu ***\n");
        out.println("1. Insert Supplier");
        out.println("2. Update Supplier");
        out.println("3. Delete Supplier");
        out.println("4. Get All Suppliers");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}
