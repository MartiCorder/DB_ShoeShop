package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShoeRepository implements cat.uvic.teknos.shoeshop.repositories.ShoeRepository{
    private Map<Integer, Shoe>shoe = new HashMap<>();
    private final String path;
    public ShoeRepository(String path){

        this.path=path;

        load();
    }

    public void load(){
        try {
            File file = new File(path);
            if (!file.exists()) {
                shoe = new HashMap<>();
                write();
            } else {
                try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                    shoe = (Map<Integer, Shoe>) inputStream.readObject();
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
            outputStream.writeObject(shoe);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Shoe model) {
        if (model.getId() <= 0){
            //get new id
            var newId=shoe.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            shoe.put(newId, model);
        }else{
            if (shoe.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            shoe.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(shoe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Shoe model) {
        shoe.remove(model.getId());
    }

    public Shoe get(Integer id) {
        return shoe.get(id);
    }

    @Override
    public Set<Shoe> getAll() {
        return Set.copyOf(shoe.values());
    }
}
