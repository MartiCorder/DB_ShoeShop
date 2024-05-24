package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Model;

import java.io.*;
import java.util.*;

public class ModelRepository implements cat.uvic.teknos.shoeshop.repositories.ModelRepository{
    private static Map<Integer, Model> models = new HashMap<>();

    private final String path;

    public ModelRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                models = (Map<Integer, Model>) inputStream.readObject();
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
    public void save(Model model) {
        if(model.getId() <= 0){
            var newId = models.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            models.put(newId, model);
        }else{
            models.put(model.getId(), model);
        }
        write();
    }
    @Override
    public void delete(Model model) {

        models.remove(model.getId());
        write();
    }

    @Override
    public Model get(Integer id) {
        return models.get(id);
    }

    @Override
    public Set<Model> getAll() {
        return Set.copyOf(models.values());
    }
}
