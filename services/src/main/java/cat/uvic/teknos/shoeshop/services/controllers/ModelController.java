package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory; // Afegim ModelFactory

    public ModelController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory; // Inicialitzem ModelFactory
    }

    @Override
    public String get(int id) {
        // Obtenim el model amb l'id passat pel repositori
        Model model = repositoryFactory.getModelRepository().get(id);

        // Si el model no existeix, retornem un error en JSON
        if (model == null) {
            return "{\"error\": \"Model not found\"}";
        }

        // Serialitzem el model en format JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting model to JSON", e);
        }
    }

    @Override
    public String get() {
        // Obtenim tots els models del repositori
        var models = repositoryFactory.getModelRepository().getAll();

        // Serialitzem la llista de models en format JSON
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

            // Deserialitzem el JSON per crear un objecte Model
            Model model = mapper.readValue(json, Model.class);

            // Comprovem que els camps essencials no siguin nuls
            if (model.getName() == null || model.getBrand() == null) {
                throw new IllegalArgumentException("Name and Brand are required fields");
            }

            // Utilitzem ModelFactory per crear una nova instància del model si cal
            Model newModel = modelFactory.createModel(); // ModelFactory hauria de tenir un mètode com aquest
            newModel.setName(model.getName());
            newModel.setBrand(model.getBrand());

            // Guardem el model creat al repositori
            repositoryFactory.getModelRepository().save(newModel);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Log de l'error de deserialització
            throw new RuntimeException("Error deserializing model from JSON", e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Log d'errors d'argument
            throw new RuntimeException("Invalid model data", e);
        } catch (Exception e) {
            e.printStackTrace(); // Log d'altres excepcions
            throw new RuntimeException("Error saving model", e);
        }
    }

    @Override
    public void put(int id, String json) {
        // Obtenim el model existent pel seu id
        Model existingModel = repositoryFactory.getModelRepository().get(id);

        if (existingModel == null) {
            throw new RuntimeException("Model not found");
        }

        // Actualitzem el model amb les noves dades del JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            Model updatedModel = mapper.readValue(json, Model.class);

            // Actualitzem els camps del model existent
            existingModel.setName(updatedModel.getName());
            existingModel.setBrand(updatedModel.getBrand());

            // Guardem el model actualitzat en el repositori
            repositoryFactory.getModelRepository().save(existingModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing model from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        // Obtenim el model existent pel seu id
        Model existingModel = repositoryFactory.getModelRepository().get(id);

        if (existingModel != null) {
            // Eliminem el model del repositori
            repositoryFactory.getModelRepository().delete(existingModel);
        } else {
            throw new RuntimeException("Model not found");
        }
    }
}
