package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;

import java.io.*;
import java.util.*;

public class ShoeRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeRepository{
    private static Map<Integer, Shoe> shoes = new HashMap<>();

    private final String path;

    public ShoeRepository(String path){
        this.path=path;
        load();
    }

     void load(){
         var file = new File(path);
         if(file.exists()) {
             try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                 shoes = (Map<Integer, Shoe>) inputStream.readObject();
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
    public void save(Shoe model) {
        if(model.getId() <= 0){
            var newId = shoes.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            shoes.put(newId, model);
        }else{
            shoes.put(model.getId(), model);
        }
        write();
    }
    @Override
    public void delete(Shoe model) {

        shoes.remove(model.getId());
        write();
    }

    @Override
    public Shoe get(Integer id) {
        return shoes.get(id);
    }

    @Override
    public Set<Shoe> getAll() {
        return Set.copyOf(shoes.values());
    }
}
