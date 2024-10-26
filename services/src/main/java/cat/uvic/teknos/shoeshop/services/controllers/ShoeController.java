package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        ShoeRepository repository = repositoryFactory.getShoeRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try{

            cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe shoe = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe.class);
            repository.save(shoe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {

        ShoeRepository repository = repositoryFactory.getShoeRepository();
        Shoe existingShoe = repository.get(id);

        if (existingShoe == null) {
            throw new RuntimeException("Shoe not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            Shoe shoeUpdated = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe.class);
            shoeUpdated.setId(id);

            existingShoe.setPrice(shoeUpdated.getPrice());
            existingShoe.setColor(shoeUpdated.getColor());
            existingShoe.setSize(shoeUpdated.getSize());
            existingShoe.setModels(shoeUpdated.getModels());
            existingShoe.setInventories(shoeUpdated.getInventories());

            repository.save(existingShoe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
