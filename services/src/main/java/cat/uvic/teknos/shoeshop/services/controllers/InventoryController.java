package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public InventoryController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Inventory inventory = repositoryFactory.getInventoryRepository().get(id);

        if (inventory == null) {
            return "{\"error\": \"Inventory not found\"}";
        }

        try {
            return mapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Inventory to JSON", e);
        }
    }

    @Override
    public String get() {
        var inventories = repositoryFactory.getInventoryRepository().getAll();

        try {
            return mapper.writeValueAsString(inventories);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Inventory list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Inventory inventory = new cat.uvic.teknos.shoeshop.file.models.Inventory();

            if (rootNode.has("CAPACITY")) {
                inventory.setCapacity(rootNode.get("CAPACITY").asInt());
            }

            if (inventory.getCapacity() == 0) {
                throw new IllegalArgumentException("Capacity is a required field");
            }

            repositoryFactory.getInventoryRepository().save(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing Inventory from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Inventory data: " + e.getMessage(), e);
        }
    }

    @Override
    public void put(int id, String json) {
        Inventory existingInventory = repositoryFactory.getInventoryRepository().get(id);

        if (existingInventory == null) {
            throw new RuntimeException("Inventory not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("CAPACITY")) {
                existingInventory.setCapacity(rootNode.get("CAPACITY").asInt());
            }

            repositoryFactory.getInventoryRepository().save(existingInventory);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing Inventory from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Inventory existingInventory = repositoryFactory.getInventoryRepository().get(id);

        if (existingInventory != null) {
            repositoryFactory.getInventoryRepository().delete(existingInventory);
        } else {
            throw new RuntimeException("Inventory not found");
        }
    }
}
