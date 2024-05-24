package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;

import java.io.*;
import java.util.*;

public class InventoryRepository implements cat.uvic.teknos.shoeshop.repositories.InventoryRepository {

    private static Map<Integer, Inventory> inventories = new HashMap<>();

    private final String path;

    public InventoryRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                inventories = (Map<Integer, Inventory>) inputStream.readObject();
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
    public void save(Inventory model) {
        if(model.getId() <= 0){
            var newId = inventories.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            inventories.put(newId, model);
        }else{
            inventories.put(model.getId(), model);
        }
        write();
    }

    @Override
    public void delete(Inventory model) {

        inventories.remove(model.getId());
        write();
    }

    @Override
    public Inventory get(Integer id) {
        return inventories.get(id);
    }

    @Override
    public Set<Inventory> getAll() {
        return Set.copyOf(inventories.values());
    }
}
