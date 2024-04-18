package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ShoeStore;


import javax.annotation.processing.SupportedAnnotationTypes;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ShoeStoreRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository{

    private static Map<Integer, ShoeStore> shoestore = new HashMap<>();

    public static void load(){

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";


        try(var inputStream = new ObjectInputStream(new FileInputStream(currentDirectory+ "shoestore.ser"))) {
            shoestore = (Map<Integer, ShoeStore>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }

    }

    public static void write(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try(var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoestore.ser"))) {
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
        write();

    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoestore.ser"))) {
            outputStream.writeObject(shoestore);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(ShoeStore model) {

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoestore.ser"))) {

            for (Iterator<Map.Entry<Integer, ShoeStore>> iterator = shoestore.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, ShoeStore> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(shoestore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoeStore get(Integer id) {
        return shoestore.get(id);
    }

    @Override
    public Set<ShoeStore> getAll() {
        return Set.copyOf(shoestore.values());
    }

}
