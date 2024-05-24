package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoeStoreRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository{

    private static Map<Integer, ShoeStore> shoeStores = new HashMap<>();

    private final String path;

    public ShoeStoreRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                shoeStores = (Map<Integer, ShoeStore>) inputStream.readObject();
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
    public void save(ShoeStore model) {
        if(model.getId() <= 0){
            var newId = shoeStores.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            shoeStores.put(newId, model);
        }else{
            shoeStores.put(model.getId(), model);
        }
        write();
    }
    @Override
    public void delete(ShoeStore model) {

        shoeStores.remove(model.getId());
        write();
    }

    @Override
    public ShoeStore get(Integer id) {
        return shoeStores.get(id);
    }

    @Override
    public Set<ShoeStore> getAll() {
        return Set.copyOf(shoeStores.values());
    }

}
