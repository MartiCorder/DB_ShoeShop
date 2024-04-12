package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryRepository implements cat.uvic.teknos.shoeshop.repositories.InventoryRepository {

    private Map<Integer, Inventory>inventory = new HashMap<>();
    private final String path;
    public InventoryRepository(String path){

        this.path=path;

        load();
    }

    public void load(){

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try(var inputStream = new ObjectInputStream(new
                FileInputStream(path))) {
            inventory = (Map<Integer, Inventory>)
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
            outputStream.writeObject(inventory);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Inventory model) {
        if (model.getId() <= 0){
            //get new id
            var newId=inventory.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            inventory.put(newId, model);
        }else{
            if (inventory.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            inventory.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(inventory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Inventory model) {
        inventory.remove(model.getId());
    }

    public Inventory get(Integer id) {
        return inventory.get(id);
    }

    @Override
    public Set<Inventory> getAll() {
        return Set.copyOf(inventory.values());
    }
}
