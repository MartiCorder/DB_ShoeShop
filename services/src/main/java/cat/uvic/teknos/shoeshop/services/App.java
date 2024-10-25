package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.controllers.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var properties = new Properties();
        properties.load(App.class.getResourceAsStream("/app.properties"));

        RepositoryFactory repositoryFactory = (RepositoryFactory) Class.forName(properties.getProperty("repositoryFactory")).getConstructor().newInstance();
        ModelFactory modelFactory = (ModelFactory) Class.forName(properties.getProperty("modelFactory")).getConstructor().newInstance();

        var controllers = new HashMap<String, Controller>();
        controllers.put("client", new ClientController(repositoryFactory, modelFactory));
        controllers.put("model", new ModelController(repositoryFactory, modelFactory));
        controllers.put("shoe", new ShoeController(repositoryFactory, modelFactory));
        controllers.put("supplier", new SupplierController(repositoryFactory, modelFactory));

        var requestRouter = new RequestRouterImpl(controllers);

        new Server(requestRouter).start();
    }
}
