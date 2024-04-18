package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ShoeRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeRepository{
    private static Map<Integer, Shoe> shoe = new HashMap<>();

    private String path;

    public ShoeRepository(String path){this.path=path;}

     void load(){

        if (Files.exists(Path.of(path))) {

             try(var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                 shoe = (Map<Integer, Shoe>) inputStream.readObject();
             } catch (FileNotFoundException e) {
                 throw new RuntimeException(e);
             } catch (IOException e) {
                 throw new RuntimeException(e);
             } catch (ClassNotFoundException e){
                 throw new RuntimeException(e);
             }
         }


    }
    void write(){


        try(var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
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
            var newId=shoe.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            shoe.put(newId, model);
        }else{
            shoe.put(model.getId(), model);
        }
        write();

    }
    public void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(shoe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Shoe model) {

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {

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
