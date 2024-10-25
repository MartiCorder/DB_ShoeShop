package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        ObjectMapper mapper = new ObjectMapper();
        try {

            Model model = mapper.readValue(json, Model.class);

            if (model.getName() == null || model.getBrand() == null) {
                throw new IllegalArgumentException("Name and Brand are required fields");
            }

            Model newModel = modelFactory.createModel();
            newModel.setName(model.getName());
            newModel.setBrand(model.getBrand());

            repositoryFactory.getModelRepository().save(newModel);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deserializing model from JSON", e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid model data", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving model", e);
        }
    }

    @Override
    public void put(int id, String json) {

        Model existingModel = repositoryFactory.getModelRepository().get(id);

        if (existingModel == null) {
            throw new RuntimeException("Model not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            Model updatedModel = mapper.readValue(json, Model.class);

            existingModel.setName(updatedModel.getName());
            existingModel.setBrand(updatedModel.getBrand());

            repositoryFactory.getModelRepository().save(existingModel);
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
