package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoeStoreController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public ShoeStoreController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        ShoeStore shoeStore = repositoryFactory.getShoeStoreRepository().get(id);

        if (shoeStore == null) {
            return "{\"error\": \"ShoeStore not found\"}";
        }

        try {
            return mapper.writeValueAsString(shoeStore);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting ShoeStore to JSON", e);
        }
    }

    @Override
    public String get() {
        var shoeStores = repositoryFactory.getShoeStoreRepository().getAll();

        try {
            return mapper.writeValueAsString(shoeStores);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting ShoeStore list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();

            if (rootNode.has("NAME")) {
                shoeStore.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("OWNER")) {
                shoeStore.setOwner(rootNode.get("OWNER").asText());
            }

            if (rootNode.has("LOCATION")) {
                shoeStore.setLocation(rootNode.get("LOCATION").asText());
            }

            if (shoeStore.getName() == null || shoeStore.getOwner() == null) {
                throw new IllegalArgumentException("Name and Owner are required fields");
            }

            repositoryFactory.getShoeStoreRepository().save(shoeStore);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing ShoeStore from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ShoeStore data: " + e.getMessage(), e);
        }
    }

    @Override
    public void put(int id, String json) {
        ShoeStore existingShoeStore = repositoryFactory.getShoeStoreRepository().get(id);

        if (existingShoeStore == null) {
            throw new RuntimeException("ShoeStore not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("NAME")) {
                existingShoeStore.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("OWNER")) {
                existingShoeStore.setOwner(rootNode.get("OWNER").asText());
            }

            if (rootNode.has("LOCATION")) {
                existingShoeStore.setLocation(rootNode.get("LOCATION").asText());
            }

            repositoryFactory.getShoeStoreRepository().save(existingShoeStore);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing ShoeStore from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        ShoeStore existingShoeStore = repositoryFactory.getShoeStoreRepository().get(id);

        if (existingShoeStore != null) {
            repositoryFactory.getShoeStoreRepository().delete(existingShoeStore);
        } else {
            throw new RuntimeException("ShoeStore not found");
        }
    }
}

