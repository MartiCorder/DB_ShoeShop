package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SupplierController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public SupplierController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Supplier supplier = repositoryFactory.getSupplierRepository().get(id);

        if (supplier == null) {
            return "{\"error\": \"Supplier not found\"}";
        }

        try {
            return mapper.writeValueAsString(supplier);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting supplier to JSON", e);
        }
    }

    @Override
    public String get() {
        var suppliers = repositoryFactory.getSupplierRepository().getAll();

        try {
            return mapper.writeValueAsString(suppliers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting suppliers list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Supplier supplier = new cat.uvic.teknos.shoeshop.file.models.Supplier();

            if (rootNode.has("NAME")) {
                supplier.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("PHONE")) {
                supplier.setPhone(rootNode.get("PHONE").asText());
            }

            if (rootNode.has("SHOE_STORE_ID")) {
                ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                shoeStore.setId(rootNode.get("SHOE_STORE_ID").asInt());
                supplier.getShoeStores().add(shoeStore);
            }

            if (supplier.getName() == null || supplier.getPhone() == null) {
                throw new IllegalArgumentException("Name and Phone are required fields");
            }

            repositoryFactory.getSupplierRepository().save(supplier);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing supplier from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid supplier data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving supplier", e);
        }
    }

    @Override
    public void put(int id, String json) {
        Supplier existingSupplier = repositoryFactory.getSupplierRepository().get(id);

        if (existingSupplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("NAME")) {
                existingSupplier.setName(rootNode.get("NAME").asText());
            }

            if (rootNode.has("PHONE")) {
                existingSupplier.setPhone(rootNode.get("PHONE").asText());
            }

            if (rootNode.has("SHOE_STORE_ID")) {
                ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                shoeStore.setId(rootNode.get("SHOE_STORE_ID").asInt());
                existingSupplier.getShoeStores().add(shoeStore);
            }

            repositoryFactory.getSupplierRepository().save(existingSupplier);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing supplier from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Supplier existingSupplier = repositoryFactory.getSupplierRepository().get(id);

        if (existingSupplier != null) {
            repositoryFactory.getSupplierRepository().delete(existingSupplier);
        } else {
            throw new RuntimeException("Supplier not found");
        }
    }
}
