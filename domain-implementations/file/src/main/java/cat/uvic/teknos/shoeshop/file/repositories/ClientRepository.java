package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.*;

public class ClientRepository implements cat.uvic.teknos.shoeshop.repositories.ClientRepository {

    private static Map<Integer, Client> client = new HashMap<>();

    public static void load() {

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";


        try (var inputStream = new ObjectInputStream(new FileInputStream(currentDirectory + "client.ser"))) {
            client = (Map<Integer, Client>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void write() {
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "client.ser"))) {
            outputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Client model) {
        if (model.getId() <= 0) {
            //get new id
            var newId = client.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            client.put(newId, model);
        } else {
            client.put(model.getId(), model);
        }
        write();

    }

    public static void update() {
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "client.ser"))) {
            outputStream.writeObject(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Client model) {

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "client.ser"))) {

            for (Iterator<Map.Entry<Integer, Client>> iterator = client.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Client> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client get(Integer id) {
        return client.get(id);
    }

    @Override
    public Set<Client> getAll() {
        return Set.copyOf(client.values());
    }
}
