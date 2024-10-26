package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

        ShoeStoreRepository repository = repositoryFactory.getShoeStoreRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {


            cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore shoeStore = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore.class);
            repository.save(shoeStore);
            }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {

        ShoeStoreRepository repository = repositoryFactory.getShoeStoreRepository();
        ShoeStore existingShoeStore = repository.get(id);

        if (existingShoeStore == null) {
            throw new RuntimeException("ShoeStore not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            ShoeStore shoeStoreUpdated = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore.class);
            shoeStoreUpdated.setId(id);

                existingShoeStore.setName(shoeStoreUpdated.getName());
                existingShoeStore.setOwner(shoeStoreUpdated.getOwner());
                existingShoeStore.setLocation(shoeStoreUpdated.getLocation());
                existingShoeStore.setSuppliers(shoeStoreUpdated.getSuppliers());
                existingShoeStore.setClients(shoeStoreUpdated.getClients());
                existingShoeStore.setInventories(shoeStoreUpdated.getInventories());

                repository.save(existingShoeStore);

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

