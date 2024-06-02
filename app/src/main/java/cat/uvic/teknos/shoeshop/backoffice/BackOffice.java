package cat.uvic.teknos.shoeshop.backoffice;

import java.io.*;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import static cat.uvic.teknos.shoeshop.backoffice.IOUtils.*;

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

    public void start() throws IOException {
        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> managerClient();
                case "2" -> managerShoe();
                case "3" -> managerShoeStore();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Program Finished ***\n");
    }

    private void managerClient() throws IOException {
        new ClientManager(in, out, repositoryFactory, modelFactory).start();
    }


    private void managerShoe() throws IOException {
        new ShoeManager(in, out, repositoryFactory, modelFactory).start();
    }

    private void managerShoeStore() throws IOException {
        new ShoeStoreManager(in, out, repositoryFactory.getShoeStoreRepository(), modelFactory).start();
    }


    private void showWelcomeMessage(){
        out.println("\n*** Welcome to the ShoeShop Back Office ***\n");
        out.println("Select a menu option:");
        out.println();
    }

    private void showMainMenu(){
        out.println("\n*** Main Menu ***\n");
        out.println("1. Manage Client");
        out.println("2. Manage Shoe");
        out.println("3. Manage Shoe Store");
        out.println("\nType 'exit' to quit.");
    }
}
