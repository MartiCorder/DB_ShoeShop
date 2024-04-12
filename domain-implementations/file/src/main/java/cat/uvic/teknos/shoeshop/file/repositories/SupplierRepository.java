package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Supplier;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SupplierRepository implements cat.uvic.teknos.shoeshop.repositories.SupplierRepository{

    private Map<Integer, Supplier>supplier = new HashMap<>();
    private final String path;
    public SupplierRepository(String path){

        this.path=path;

        load();
    }

    public void load(){

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try(var inputStream = new ObjectInputStream(new
                FileInputStream(path))) {
            supplier = (Map<Integer, Supplier>)
                    inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void write(){
        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try(var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(supplier);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Supplier model) {
        if (model.getId() <= 0){
            //get new id
            var newId=supplier.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            supplier.put(newId, model);
        }else{
            if (supplier.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            supplier.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(supplier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Supplier model) {
        supplier.remove(model.getId());
    }

    public Supplier get(Integer id) {
        return supplier.get(id);
    }

    @Override
    public Set<Supplier> getAll() {
        return Set.copyOf(supplier.values());
    }
}
