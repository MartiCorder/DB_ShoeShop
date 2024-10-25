package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoeController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public ShoeController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Shoe shoe = repositoryFactory.getShoeRepository().get(id);

        if (shoe == null) {
            return "{\"error\": \"Shoe not found\"}";
        }

        try {
            return mapper.writeValueAsString(shoe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting shoe to JSON", e);
        }
    }

    @Override
    public String get() {
        var shoes = repositoryFactory.getShoeRepository().getAll();

        try {
            return mapper.writeValueAsString(shoes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting shoe list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Shoe shoe = new cat.uvic.teknos.shoeshop.file.models.Shoe();

            if (rootNode.has("PRICE")) {
                shoe.setPrice(rootNode.get("PRICE").asDouble());
            }

            if (rootNode.has("COLOR")) {
                shoe.setColor(rootNode.get("COLOR").asText());
            }

            if (rootNode.has("SIZE")) {
                shoe.setSize(rootNode.get("SIZE").asText());
            }

            if (rootNode.has("MODEL_ID")) {
                var model = modelFactory.createModel();
                model.setId(rootNode.get("MODEL_ID").asInt());
                shoe.setModels(model);
            }

            if (rootNode.has("INVENTORY_ID")) {
                Inventory inventory = new cat.uvic.teknos.shoeshop.file.models.Inventory();
                inventory.setId(rootNode.get("INVENTORY_ID").asInt());
                shoe.setInventories(inventory);
            }

            if (shoe.getPrice() <= 0 || shoe.getColor() == null || shoe.getSize() == null) {
                throw new IllegalArgumentException("Price, Color, and Size are required fields");
            }

            repositoryFactory.getShoeRepository().save(shoe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing shoe from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid shoe data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving shoe", e);
        }
    }

    @Override
    public void put(int id, String json) {
        Shoe existingShoe = repositoryFactory.getShoeRepository().get(id);

        if (existingShoe == null) {
            throw new RuntimeException("Shoe not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("PRICE")) {
                existingShoe.setPrice(rootNode.get("PRICE").asDouble());
            }

            if (rootNode.has("COLOR")) {
                existingShoe.setColor(rootNode.get("COLOR").asText());
            }

            if (rootNode.has("SIZE")) {
                existingShoe.setSize(rootNode.get("SIZE").asText());
            }

            if (rootNode.has("MODEL_ID")) {
                var model = modelFactory.createModel();
                model.setId(rootNode.get("MODEL_ID").asInt());
                existingShoe.setModels(model);
            }

            if (rootNode.has("INVENTORY_ID")) {
                Inventory inventory = new cat.uvic.teknos.shoeshop.file.models.Inventory();
                inventory.setId(rootNode.get("INVENTORY_ID").asInt());
                existingShoe.setInventories(inventory);
            }

            repositoryFactory.getShoeRepository().save(existingShoe);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing shoe from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Shoe existingShoe = repositoryFactory.getShoeRepository().get(id);

        if (existingShoe != null) {
            repositoryFactory.getShoeRepository().delete(existingShoe);
        } else {
            throw new RuntimeException("Shoe not found");
        }
    }
}
