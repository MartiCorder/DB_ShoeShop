package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelRepository implements cat.uvic.teknos.shoeshop.repositories.ModelRepository{
    private Map<Integer, Model>model1 = new HashMap<>();
    private final String path;
    public ModelRepository(String path){

        this.path=path;

        load();
    }

    public void load(){
        try {
            File file = new File(path);
            if (!file.exists()) {
                model1 = new HashMap<>();
                write();
            } else {
                try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                    model1 = (Map<Integer, Model>) inputStream.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(){
        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try(var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(model1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Model model) {
        if (model.getId() <= 0){
            //get new id
            var newId=model1.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            model1.put(newId, model);
        }else{
            if (model1.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            model1.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(model1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Model model) {
        model1.remove(model.getId());
    }

    public Model get(Integer id) {
        return model1.get(id);
    }

    @Override
    public Set<Model> getAll() {
        return Set.copyOf(model1.values());
    }
}
