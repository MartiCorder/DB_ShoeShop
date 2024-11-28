package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.controllers.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        try {
            // Carregar propietats
            var properties = new Properties();
            properties.load(App.class.getResourceAsStream("/app.properties"));

            // Crear les instàncies de les fàbriques
            RepositoryFactory repositoryFactory = (RepositoryFactory) Class.forName(properties.getProperty("repositoryFactory"))
                    .getConstructor().newInstance();
            ModelFactory modelFactory = (ModelFactory) Class.forName(properties.getProperty("modelFactory"))
                    .getConstructor().newInstance();

            // Crear els controladors
            var controllers = new HashMap<String, Controller>();
            controllers.put("clients", new ClientController(repositoryFactory, modelFactory));
            controllers.put("models", new ModelController(repositoryFactory, modelFactory));
            controllers.put("shoes", new ShoeController(repositoryFactory, modelFactory));
            controllers.put("suppliers", new SupplierController(repositoryFactory, modelFactory));
            controllers.put("inventories", new InventoryController(repositoryFactory, modelFactory));
            controllers.put("shoeStores", new ShoeStoreController(repositoryFactory, modelFactory));
            controllers.put("addresses", new AddressController(repositoryFactory, modelFactory));

            // Alias i contrasenya per al keystore
            String alias = "client1";
            char[] password = "Teknos01.".toCharArray();

            // Ruta al fitxer del keystore
            String p12File = Objects.requireNonNull(App.class.getResource("/client1.p12")).getPath();

            // Inicialitzar el KeyStore i carregar la clau privada
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(p12File)) {
                keyStore.load(fis, password);
            }

            // Comprovar els alias disponibles al KeyStore
            for (String availableAlias : Collections.list(keyStore.aliases())) {
                System.out.println("Available alias: " + availableAlias);
            }

            PrivateKey serverPrivateKey = (PrivateKey) keyStore.getKey(alias, password);

            // Crear el RequestRouter amb la clau privada
            var requestRouter = new RequestRouterImpl(controllers, serverPrivateKey);

            // Iniciar el servidor amb el RequestRouter
            int maxThreads = Integer.parseInt(properties.getProperty("maxThreads", "10"));
            Server server = new Server(requestRouter, maxThreads);
            server.start();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.println("Error al iniciar l'aplicació: " + e.getMessage());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
            System.err.println("Error al carregar la clau privada des del keystore: " + e.getMessage());
            e.printStackTrace();  // Mostrar detalls del error
        }
    }
}