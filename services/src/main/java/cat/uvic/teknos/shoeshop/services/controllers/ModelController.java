package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;

    public ModelController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    @Override
    public String get(int id) {
        Model model = repositoryFactory.getModelRepository().get(id);

        if (model == null) {
            return "{\"error\": \"Model not found\"}";
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting model to JSON", e);
        }
    }

    @Override
    public String get() {

        var models = repositoryFactory.getModelRepository().getAll();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(models);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting models list to JSON", e);
        }
    }

    @Override
    public void post(String json) {

        ModelRepository repository = repositoryFactory.getModelRepository();
        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {

            cat.uvic.teknos.shoeshop.domain.jdbc.models.Model model = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Model.class);

            repository.save(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {

        ModelRepository repository = repositoryFactory.getModelRepository();
        Model existingModel = repository.get(id);

        if (existingModel == null) {
            throw new RuntimeException("Model not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            Model updatedModel = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Model.class);

            existingModel.setName(updatedModel.getName());
            existingModel.setBrand(updatedModel.getBrand());

            repository.save(existingModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing model from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Model existingModel = repositoryFactory.getModelRepository().get(id);

        if (existingModel != null) {
            repositoryFactory.getModelRepository().delete(existingModel);
        } else {
            throw new RuntimeException("Model not found");
        }
    }
}
