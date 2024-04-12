package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoeStoreRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository{

    private Map<Integer, ShoeStore>shoestore = new HashMap<>();
    private final String path;
    public ShoeStoreRepository(String path){

        this.path=path;

        load();
    }

    public void load(){

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try(var inputStream = new ObjectInputStream(new FileInputStream(path))) {
            shoestore = (Map<Integer, ShoeStore>) inputStream.readObject();
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
            outputStream.writeObject(shoestore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ShoeStore model) {
        if (model.getId() <= 0){
        //get new id
            var newId=shoestore.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            shoestore.put(newId, model);
        }else{
            if (shoestore.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            shoestore.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(shoestore);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(ShoeStore model) {
        shoestore.remove(model.getId());
    }

    public ShoeStore get(Integer id) {
        return shoestore.get(id);
    }

    @Override
    public Set<ShoeStore> getAll() {
        return Set.copyOf(shoestore.values());
    }

}
