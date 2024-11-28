package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.controllers.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        try {
            Properties properties = loadProperties("/app.properties");

            RepositoryFactory repositoryFactory = createInstance(properties.getProperty("repositoryFactory"), RepositoryFactory.class);
            ModelFactory modelFactory = createInstance(properties.getProperty("modelFactory"), ModelFactory.class);

            Map<String, Controller> controllers = initializeControllers(repositoryFactory, modelFactory);

            RequestRouterImpl requestRouter = new RequestRouterImpl(controllers);
            Server server = new Server(requestRouter);

            int maxThreads = Integer.parseInt(properties.getProperty("maxThreads", "10"));
            System.out.println("Starting server with maxThreads: " + maxThreads);
            server.start();

        } catch (Exception e) {
            System.err.println("Error starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(String resourcePath) throws IOException {
        try (InputStream input = App.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Properties file not found at: " + resourcePath);
            }
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }

    private static <T> T createInstance(String className, Class<T> type)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("Class name for " + type.getSimpleName() + " is missing in properties.");
        }
        Class<?> clazz = Class.forName(className);
        return type.cast(clazz.getConstructor().newInstance());
    }

    private static Map<String, Controller> initializeControllers(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        Map<String, Controller> controllers = new HashMap<>();
        controllers.put("clients", new ClientController(repositoryFactory, modelFactory));
        controllers.put("models", new ModelController(repositoryFactory, modelFactory));
        controllers.put("shoes", new ShoeController(repositoryFactory, modelFactory));
        controllers.put("suppliers", new SupplierController(repositoryFactory, modelFactory));
        controllers.put("inventories", new InventoryController(repositoryFactory, modelFactory));
        controllers.put("shoeStores", new ShoeStoreController(repositoryFactory, modelFactory));
        controllers.put("addresses", new AddressController(repositoryFactory, modelFactory));

        return controllers;
    }
}
