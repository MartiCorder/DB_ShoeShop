package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Model;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class ModelRepository implements cat.uvic.teknos.shoeshop.repositories.ModelRepository{
    private static Map<Integer, Model> model1;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            model1 = (Map<Integer, Model>) inputStream.readObject();
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
            //get new id
            var newId=model1.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            model1.put(newId, model);
        }else{
            model1.put(model.getId(), model);
        }

    }

    @Override
    public void delete(Model model) {
        model1.remove(model.getId());
    }

    @Override
    public Model get(Integer id) {
        return null;
    }

    @Override
    public Set<Model> getAll() {
        return null;
    }
}
