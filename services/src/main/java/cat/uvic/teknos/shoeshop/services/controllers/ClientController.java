package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory; // Importem ModelFactory
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory; // Afegim ModelFactory

    public ClientController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory; // Inicialitzem ModelFactory
    }

    @Override
    public String get(int id) {
        // Obtenim el client amb l'id passat pel repositori
        Client client = repositoryFactory.getClientRepository().get(id);

        // Si el client no existeix, retornem un error en JSON
        if (client == null) {
            return "{\"error\": \"Client not found\"}";
        }

        // Serialitzem el client en format JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting client to JSON", e);
        }
    }

    @Override
    public String get() {
        // Obtenim tots els clients del repositori
        var clients = repositoryFactory.getClientRepository().getAll();

        // Serialitzem la llista de clients en format JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(clients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting clients list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Client client = mapper.readValue(json, Client.class);

            // Assegura't que el client no sigui null i que els camps necessaris estiguin configurats
            if (client.getDni() == null || client.getName() == null) {
                throw new IllegalArgumentException("DNI and Name are required fields");
            }

            repositoryFactory.getClientRepository().save(client);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Log de l'error de deserialització
            throw new RuntimeException("Error deserializing client from JSON", e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Log d'errors d'argument
            throw new RuntimeException("Invalid client data", e);
        } catch (Exception e) {
            e.printStackTrace(); // Log d'altres excepcions
            throw new RuntimeException("Error saving client", e);
        }
    }

    @Override
    public void put(int id, String json) {
        // Obtenim el client existent pel seu id
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient == null) {
            throw new RuntimeException("Client not found");
        }

        // Actualitzem el client amb les noves dades del JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            Client updatedClient = mapper.readValue(json, Client.class);

            // Actualitzem els camps del client existent
            existingClient.setDni(updatedClient.getDni());
            existingClient.setName(updatedClient.getName());
            existingClient.setPhone(updatedClient.getPhone());
            existingClient.setAddresses(updatedClient.getAddresses());
            existingClient.setShoeStores(updatedClient.getShoeStores());

            // Guardem el client actualitzat en el repositori
            repositoryFactory.getClientRepository().save(existingClient);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing client from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        // Obtenim el client existent pel seu id
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient != null) {
            // Eliminem el client del repositori
            repositoryFactory.getClientRepository().delete(existingClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }
}
