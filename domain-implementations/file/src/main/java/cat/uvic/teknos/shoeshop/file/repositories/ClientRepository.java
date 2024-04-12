package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientRepository implements cat.uvic.teknos.shoeshop.repositories.ClientRepository{

    private Map<Integer, Client>client = new HashMap<>();
    private final String path;
    public ClientRepository(String path){

        this.path=path;

        load();
    }

    public void load(){

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try(var inputStream = new ObjectInputStream(new
                FileInputStream(path))) {
            client = (Map<Integer, Client>)
                    inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void write(){
        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try(var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Client model) {
        if (model.getId() <= 0){
            //get new id
            var newId=client.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            client.put(newId, model);
        }else{
            if (client.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            client.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Client model) {
        client.remove(model.getId());
    }

    public Client get(Integer id) {
        return client.get(id);
    }

    @Override
    public Client get(String name) {
        return client.get(name);
    }

    @Override
    public Set<Client> getAll() {
        return Set.copyOf(client.values());
    }
}
