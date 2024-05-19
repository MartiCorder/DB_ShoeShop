package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.AddressRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.readLine;

public class AddressManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final AddressRepository addressRepository;
    private final ModelFactory modelFactory;

    public AddressManager(BufferedReader in, PrintStream out, AddressRepository addressRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.addressRepository = addressRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        out.println("\n*** Address Management ***\n");

        var command = "";
        do {
            showAddressMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> getAll();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Exiting Address Management ***\n");
    }


    private void getAll() {
        out.println("\n*** List of Address ***\n");
        var addresses = addressRepository.getAll();

        out.println(AsciiTable.getTable(addresses, Arrays.asList(
                new Column().header("Id").with(address -> String.valueOf(address.getId())),
                new Column().header("Location").with(Address::getLocation)
        )));
    }

    private void delete() {
        out.println("\n*** Delete Address ***\n");

        var address = modelFactory.createAddress();

        out.println("Enter the ID of the address to delete:");
        int id = Integer.parseInt(readLine(in));
        address.setId(id);

        addressRepository.delete(address);
        out.println("\nSuccessfully deleted.\n");
    }

    private void update() {
        out.println("\n*** Update Address ***\n");

        try {
            var address = modelFactory.createAddress();

            out.println("Enter the ID of the address to update:");
            int id = Integer.parseInt(readLine(in));
            address.setId(id);

            out.println("Enter new location:");
            address.setLocation(readLine(in));

            addressRepository.save(address);
            out.println("\nSuccessfully updated.\n");

        } catch (NumberFormatException e) {
            out.println("\nInvalid address ID. Please enter a valid integer ID.\n");
        } catch (Exception e) {
            out.println("\nAn error occurred while updating the address: " + e.getMessage() + "\n");
        }
    }

    private void insert() {
        out.println("\n*** Insert Address ***\n");

        var address = modelFactory.createAddress();

        out.println("Enter the location:");
        address.setLocation(readLine(in));

        addressRepository.save(address);
        out.println("\nSuccessfully inserted.\n");
    }

    private void showAddressMenu() {
        out.println("\n*** Address Management Menu ***\n");
        out.println("1. Insert Address");
        out.println("2. Update Address");
        out.println("3. Delete Address");
        out.println("4. Get All Addresses");
        out.println("Type 'exit' to quit.");
        out.println();
    }
}