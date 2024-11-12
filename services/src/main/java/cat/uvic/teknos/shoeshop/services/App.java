package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.controllers.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        try {
            var properties = new Properties();
            properties.load(App.class.getResourceAsStream("/app.properties"));

            RepositoryFactory repositoryFactory = (RepositoryFactory) Class.forName(properties.getProperty("repositoryFactory"))
                    .getConstructor().newInstance();
            ModelFactory modelFactory = (ModelFactory) Class.forName(properties.getProperty("modelFactory"))
                    .getConstructor().newInstance();

            var controllers = new HashMap<String, Controller>();
            controllers.put("clients", new ClientController(repositoryFactory, modelFactory));
            controllers.put("models", new ModelController(repositoryFactory, modelFactory));
            controllers.put("shoes", new ShoeController(repositoryFactory, modelFactory));
            controllers.put("suppliers", new SupplierController(repositoryFactory, modelFactory));
            controllers.put("inventories", new InventoryController(repositoryFactory, modelFactory));
            controllers.put("shoeStores", new ShoeStoreController(repositoryFactory, modelFactory));
            controllers.put("addresses", new AddressController(repositoryFactory, modelFactory));

            var requestRouter = new RequestRouterImpl(controllers);

            int maxThreads = Integer.parseInt(properties.getProperty("maxThreads", "10"));
            Server server = new Server(requestRouter, maxThreads);
            server.start();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.println("Error al iniciar l'aplicació: " + e.getMessage());
        }
    }
}
