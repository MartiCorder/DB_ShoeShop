package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Client;

import java.io.*;
import java.util.*;

public class ClientRepository implements cat.uvic.teknos.shoeshop.repositories.ClientRepository {

    private static Map<Integer, Client> clients = new HashMap<>();

    private final String path;

    public ClientRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                clients = (Map<Integer, Client>) inputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void write(){
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))){
            outputStream.writeObject(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Client model) {
        if(model.getId() <= 0){
            var newId = clients.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            clients.put(newId, model);
        }else{
            clients.put(model.getId(), model);
        }
        write();
    }
    @Override
    public void delete(Client model) {

        clients.remove(model.getId());
        write();
    }

    @Override
    public Client get(Integer id) {
        return clients.get(id);
    }


    @Override
    public Set<Client> getAll() {
        return Set.copyOf(clients.values());
    }
}
