package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddressController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public AddressController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Address address = repositoryFactory.getAddressRepository().get(id);

        if (address == null) {
            return "{\"error\": \"Address not found\"}";
        }

        try {
            return mapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting address to JSON", e);
        }
    }

    @Override
    public String get() {
        var addresses = repositoryFactory.getAddressRepository().getAll();

        try {
            return mapper.writeValueAsString(addresses);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting address list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Address address = new cat.uvic.teknos.shoeshop.file.models.Address();

            if (rootNode.has("LOCATION")) {
                address.setLocation(rootNode.get("LOCATION").asText());
            }

            if (address.getLocation() == null) {
                throw new IllegalArgumentException("Location is a required field");
            }

            repositoryFactory.getAddressRepository().save(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing address from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid address data: " + e.getMessage(), e);
        }
    }

    @Override
    public void put(int id, String json) {
        Address existingAddress = repositoryFactory.getAddressRepository().get(id);

        if (existingAddress == null) {
            throw new RuntimeException("Address not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);

            if (rootNode.has("LOCATION")) {
                existingAddress.setLocation(rootNode.get("LOCATION").asText());
            }

            repositoryFactory.getAddressRepository().save(existingAddress);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing address from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Address existingAddress = repositoryFactory.getAddressRepository().get(id);

        if (existingAddress != null) {
            repositoryFactory.getAddressRepository().delete(existingAddress);
        } else {
            throw new RuntimeException("Address not found");
        }
    }
}
