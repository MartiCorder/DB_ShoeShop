package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public ClientController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Client client = repositoryFactory.getClientRepository().get(id);

        if (client == null) {
            return "{\"error\": \"Client not found\"}";
        }
        try {
            return mapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting client to JSON", e);
        }
    }

    @Override
    public String get() {
        var clients = repositoryFactory.getClientRepository().getAll();
        try {
            return mapper.writeValueAsString(clients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting clients list to JSON", e);
        }
    }

    @Override
    public void post(String json) {

        ClientRepository repository = repositoryFactory.getClientRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try{
            cat.uvic.teknos.shoeshop.domain.jdbc.models.Client client = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Client.class);
            repository.save(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(int id, String json) {

        ClientRepository repository = repositoryFactory.getClientRepository();
        Client existingClient = repository.get(id);

        if (existingClient == null) {
            throw new RuntimeException("Client not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            Client clientUpdated = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Client.class);
            clientUpdated.setId(id);

            existingClient.setName(clientUpdated.getName());
            existingClient.setDni(clientUpdated.getDni());
            existingClient.setPhone(clientUpdated.getPhone());
            existingClient.setAddresses(clientUpdated.getAddresses());
            existingClient.setShoeStores(clientUpdated.getShoeStores());


            repository.save(existingClient);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient != null) {
            repositoryFactory.getClientRepository().delete(existingClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }
}
