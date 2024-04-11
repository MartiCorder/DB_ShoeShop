package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class ShoeRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeRepository{
    private static Map<Integer, Shoe> shoe;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            shoe = (Map<Integer, Shoe>) inputStream.readObject();
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
            outputStream.writeObject(shoe);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Shoe model) {
        if (model.getId() <= 0){
            //get new id
            var newId=shoe.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            shoe.put(newId, model);
        }else{
            shoe.put(model.getId(), model);
        }

    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoe.ser"))) {
            outputStream.writeObject(shoe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Shoe model) {
        shoe.remove(model.getId());


    }

    @Override
    public Shoe get(Integer id) {
        return null;
    }

    @Override
    public Set<Shoe> getAll() {
        return null;
    }
}
