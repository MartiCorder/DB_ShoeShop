package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;


import java.io.*;
import java.util.Map;
import java.util.Set;

public class ShoeStoreRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository{

    private static Map<Integer, ShoeStore> shoestore;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            shoestore = (Map<Integer, ShoeStore>) inputStream.readObject();
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
            outputStream.writeObject(shoestore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(ShoeStore model) {
        if (model.getId() <= 0){
            //get new id
            var newId=shoestore.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            shoestore.put(newId, model);
        }else{
            shoestore.put(model.getId(), model);
        }
    }
    @Override
    public void delete(ShoeStore model) {
        shoestore.remove(model.getId());
    }

    public ShoeStore get(Integer id) {
        return null;
    }

    @Override
    public Set<ShoeStore> getAll() {
        return null;
    }
}
