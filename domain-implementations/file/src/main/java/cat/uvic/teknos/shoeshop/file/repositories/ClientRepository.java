package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Client;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class ClientRepository implements cat.uvic.teknos.shoeshop.repositories.ClientRepository{

    private static Map<Integer, Client> client;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            client = (Map<Integer, Client>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static void write(){

        try(var outputStream = new ObjectOutputStream(new FileOutputStream(""))) {
            outputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Client model) {
        if (model.getId() <= 0){
            //get new id
            var newId=client.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            client.put(newId, model);
        }else{
            client.put(model.getId(), model);
        }

    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "client.ser"))) {
            outputStream.writeObject(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Client model) {
        client.remove(model.getId());
    }

    @Override
    public Client get(Integer id) {
        return null;
    }

    @Override
    public Client get(String name) {
        return null;
    }

    @Override
    public Set<Client> getAll() {
        return null;
    }
}
