package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;

import java.io.*;
import java.util.*;

public class ShoeRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeRepository{
    private static Map<Integer, Shoe> shoe = new HashMap<>();

    public static void load(){

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";


        try(var inputStream = new ObjectInputStream(new FileInputStream(currentDirectory+ "shoe.ser"))) {
            shoe = (Map<Integer, Shoe>) inputStream.readObject();
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

        try(var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoe.ser"))) {
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
        write();

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

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "shoe.ser"))) {

            for (Iterator<Map.Entry<Integer, Shoe>> iterator = shoe.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Shoe> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(shoe);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Shoe get(Integer id) {
        return shoe.get(id);
    }

    @Override
    public Set<Shoe> getAll() {
        return Set.copyOf(shoe.values());
    }
}
