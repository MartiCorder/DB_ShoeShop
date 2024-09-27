package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientController implements Controller<Integer, Client> {
    @Override
    public String get(Integer integer) {
        //retrieve (get) client from db
        // serialize client in json format

        return ""; //json
    }

    @Override
    public String get() {
        //clients = clientRepository.getAll();
        var objectMapper = new ObjectMapper();
        var json = objectMapper;
        return "";//json amb tots els estudiants
    }

    @Override
    public void post(Client value) {

    }

    @Override
    public void put(Integer key, Client value) {

    }

    @Override
    public void delete(Integer key) {

    }
}
