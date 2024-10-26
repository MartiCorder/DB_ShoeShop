package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public InventoryController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = Mappers.get();
    }

    @Override
    public String get(int id) {
        Inventory inventory = repositoryFactory.getInventoryRepository().get(id);
        if (inventory == null) return "{\"error\": \"Inventory not found\"}";
        try {
            return mapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting inventory to JSON", e);
        }
    }

    @Override
    public String get() {
        var inventories = repositoryFactory.getInventoryRepository().getAll();
        try {
            return mapper.writeValueAsString(inventories);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting inventory list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        InventoryRepository repository = repositoryFactory.getInventoryRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);


        try {
            cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory inventory = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory.class);
            repository.save(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {
        InventoryRepository repository = repositoryFactory.getInventoryRepository();
        Inventory existingInventory = repository.get(id);
        if (existingInventory == null) throw new RuntimeException("Inventory not found");

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            Inventory inventoryUpdated = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory.class);
            inventoryUpdated.setId(id);

            existingInventory.setCapacity(inventoryUpdated.getCapacity());
            existingInventory.setShoeStores(inventoryUpdated.getShoeStores());
            existingInventory.setShoes(inventoryUpdated.getShoes());

            repository.save(existingInventory);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {

        InventoryRepository repository = repositoryFactory.getInventoryRepository();
        Inventory inventory = repository.get(id);

        if (inventory == null) {
            throw new ResourceNotFoundException("Cannot delete. Team not found with id: " + id);
        }

        repository.delete(inventory);
    }
}
