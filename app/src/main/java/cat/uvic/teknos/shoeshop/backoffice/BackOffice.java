package cat.uvic.teknos.shoeshop.backoffice;

import java.io.*;
import java.sql.SQLException;

import cat.uvic.teknos.shoeshop.backoffice.exceptions.BackOfficeException;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import static cat.uvic.teknos.shoeshop.backoffice.IOUtilis.*;
public class BackOffice {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    public BackOffice(InputStream inputStream, OutputStream outputStream, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new PrintStream(outputStream);
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }
    public void start() throws SQLException {
        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command){
                case "1" -> managerAddress();
                case "2" -> managerClient();
                case "3" -> managerInventory();
                case "4" -> managerModel();
                case "5" -> managerShoe();
                case "6" -> managerShoeStore();
                case "7" -> managerSupplier();
            }
        }
        while (!command.equals("exit"));
        out.println("Program finished");
    }
    private void managerAddress() throws SQLException {
        new AddressManager(in, out,repositoryFactory.getAddressRepository(),modelFactory).start();

    }

    private void managerClient() throws SQLException {
        new ClientManager(in, out,repositoryFactory.getClientRepository(),modelFactory).start();

    }

    private void managerInventory() throws SQLException {
        new DriverManager(in, out, repositoryFactory.getInventoryRepository(),modelFactory).start();

    }

    private void managerModel() throws SQLException {
        new CarManager(in, out, repositoryFactory.getModelRepository(),modelFactory).start();
    }

    private void managerShoe() throws SQLException {
        new TeamManager(in, out, repositoryFactory.getShoeRepository(), modelFactory).start();
    }
    private void managerShoeStore() throws SQLException {
        new TeamManager(in, out, repositoryFactory.getShoeStoreRepository(), modelFactory).start();
    }
    private void managerSupplier() throws SQLException {
        new TeamManager(in, out, repositoryFactory.getSupplierRepository(), modelFactory).start();
    }
    private void showWelcomeMessage() {
        out.println("Welcome to the ShoeShop Black Office");
        out.println("Select a menu option");
        out.println(" ");
    }
    private void showMainMenu() {
        out.println("1. Address");
        out.println("2. Client");
        out.println("3. Inventory");
        out.println("4. Model");
        out.println("5. Shoe");
        out.println("6. ShoeStore");
        out.println("7. Supplier");
    }
}