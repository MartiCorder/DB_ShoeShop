package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public ClientController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Client client = repositoryFactory.getClientRepository().get(id);

        if (client == null) {
            return "{\"error\": \"Client not found\"}";
        }

        try {
            return mapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting client to JSON", e);
        }
    }

    @Override
    public String get() {
        var clients = repositoryFactory.getClientRepository().getAll();

        try {
            return mapper.writeValueAsString(clients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting clients list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Client client = new cat.uvic.teknos.shoeshop.file.models.Client();

            if (rootNode.has("DNI")) {
                client.setDni(rootNode.get("DNI").asText());
            }

            if (rootNode.has("NAME")) {
                client.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("PHONE")) {
                client.setPhone(rootNode.get("PHONE").asText());
            }

            if (rootNode.has("ADDRESS_ID")) {
                Address address = new cat.uvic.teknos.shoeshop.file.models.Address();
                address.setId(rootNode.get("ADDRESS_ID").asInt());
                client.setAddresses(address);
            }

            if (rootNode.has("SHOE_STORE_ID")) {
                ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                shoeStore.setId(rootNode.get("SHOE_STORE_ID").asInt());
                client.setShoeStores(shoeStore);
            }

            if (client.getDni() == null || client.getName() == null) {
                throw new IllegalArgumentException("DNI and Name are required fields");
            }

            repositoryFactory.getClientRepository().save(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing client from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid client data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving client", e);
        }
    }

    @Override
    public void put(int id, String json) {
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient == null) {
            throw new RuntimeException("Client not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("DNI")) {
                existingClient.setDni(rootNode.get("DNI").asText());
            }

            if (rootNode.has("NAME")) {
                existingClient.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("PHONE")) {
                existingClient.setPhone(rootNode.get("PHONE").asText());
            }

            if (rootNode.has("ADDRESS_ID")) {
                Address address = new cat.uvic.teknos.shoeshop.file.models.Address();
                address.setId(rootNode.get("ADDRESS_ID").asInt());
                if (rootNode.has("LOCATION")) {
                    address.setLocation(rootNode.get("LOCATION").asText());
                }
                existingClient.setAddresses(address);
            }

            if (rootNode.has("SHOE_STORE_ID")) {
                ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                shoeStore.setId(rootNode.get("SHOE_STORE_ID").asInt());
                if (rootNode.has("NAME")) {
                    shoeStore.setName(rootNode.get("NAME").asText());
                }
                existingClient.setShoeStores(shoeStore);
            }

            repositoryFactory.getClientRepository().save(existingClient);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing client from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient != null) {
            repositoryFactory.getClientRepository().delete(existingClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }
}
