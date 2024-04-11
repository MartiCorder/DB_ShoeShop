package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class InventoryRepository implements cat.uvic.teknos.shoeshop.repositories.InventoryRepository {

    private static Map<Integer, Inventory> inventory;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            inventory = (Map<Integer, Inventory>) inputStream.readObject();
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
            //get new id
            var newId=inventory.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            inventory.put(newId, model);
        }else{
            inventory.put(model.getId(), model);
        }

    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "inventory.ser"))) {
            outputStream.writeObject(inventory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Inventory model) {
        inventory.remove(model.getId());
    }

    @Override
    public Inventory get(Integer id) {
        return null;
    }

    @Override
    public Set<Inventory> getAll() {
        return null;
    }
}
