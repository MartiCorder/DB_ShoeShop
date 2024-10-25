package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.AddressRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

        AddressRepository repository = repositoryFactory.getAddressRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            cat.uvic.teknos.shoeshop.domain.jdbc.models.Address address = mapper.readValue(json,cat.uvic.teknos.shoeshop.domain.jdbc.models.Address.class );
            repository.save(address);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {

        AddressRepository repository = repositoryFactory.getAddressRepository();
        Address existingAddress = repository.get(id);

        if (existingAddress == null) {
            throw new RuntimeException("Address not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            Address addressUpdated = mapper.readValue(json,cat.uvic.teknos.shoeshop.domain.jdbc.models.Address.class );
            addressUpdated.setId(id);

            existingAddress.setLocation(addressUpdated.getLocation());

            repository.save(existingAddress);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
