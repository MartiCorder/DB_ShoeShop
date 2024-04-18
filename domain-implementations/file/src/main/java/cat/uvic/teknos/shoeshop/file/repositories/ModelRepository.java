package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModelRepository implements cat.uvic.teknos.shoeshop.repositories.ModelRepository{
    private static Map<Integer, Model> model1 = new HashMap<>();

    private String path;

    public ModelRepository(String path){this.path=path;}

    void load(){

        if (Files.exists(Path.of(path))) {

            try(var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                model1 = (Map<Integer, Model>) inputStream.readObject();
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
            outputStream.writeObject(model1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Model model) {
        if (model.getId() <= 0){
            var newId=model1.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            model1.put(newId, model);
        }else{
            model1.put(model.getId(), model);
        }
        write();

    }
    public void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(model1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Model model) {

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {

            for (Iterator<Map.Entry<Integer, Model>> iterator = model1.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Model> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(model1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Model get(Integer id) {
        return model1.get(id);
    }

    @Override
    public Set<Model> getAll() {
        return Set.copyOf(model1.values());
    }
}
