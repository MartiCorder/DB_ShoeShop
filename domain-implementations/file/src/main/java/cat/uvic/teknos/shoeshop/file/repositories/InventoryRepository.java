package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class InventoryRepository implements cat.uvic.teknos.shoeshop.repositories.InventoryRepository {

    private static Map<Integer, Inventory> inventory = new HashMap<>();

    private String path;

    public InventoryRepository(String path){this.path=path;}

    void load(){

        if (Files.exists(Path.of(path))) {

            try(var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                inventory = (Map<Integer, Inventory>) inputStream.readObject();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }


    }
    void write(){


        try(var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(inventory);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Inventory model) {
        if (model.getId() <= 0){
            var newId=inventory.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            inventory.put(newId, model);
        }else{
            inventory.put(model.getId(), model);
        }
        write();

    }
    public void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(inventory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Inventory model) {

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {

            for (Iterator<Map.Entry<Integer, Inventory>> iterator = inventory.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Inventory> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(inventory);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inventory get(Integer id) {
        return inventory.get(id);
    }

    @Override
    public Set<Inventory> getAll() {
        return Set.copyOf(inventory.values());
    }
}
