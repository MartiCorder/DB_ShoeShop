package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientController implements Controller{

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;

    public ClientController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;

    }
    @Override
    public String get(int id) {
        //retrieve (get) client from db
        // serialize client in json format

        return ""; //json
    }

    @Override
    public String get() {
        var clients = repositoryFactory.getClientRepository().getAll();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(clients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void post(String json) {
        //repositoryFactory.getStudentRepository().save(value);

    }

    @Override
    public void put(int id, String json) {

    }

    @Override
    public void delete(int id) {

    }
}
