package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.Supplier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SupplierRepository implements cat.uvic.teknos.shoeshop.repositories.SupplierRepository, Serializable{

    private static Map<Integer, Supplier> supplier = new HashMap<>();

    private String path;

    public SupplierRepository(String path){this.path=path;}

    void load(){

        if (Files.exists(Path.of(path))) {

            try(var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                supplier = (Map<Integer, Supplier>) inputStream.readObject();
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
            outputStream.writeObject(supplier);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Supplier model) {
        if (model.getId() <= 0){
            var newId=supplier.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            supplier.put(newId, model);
        }else{
            supplier.put(model.getId(), model);
        }
        write();

    }
    public void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(supplier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Supplier model) {

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(path))) {

            for (Iterator<Map.Entry<Integer, Supplier>> iterator = supplier.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Supplier> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(supplier);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier get(Integer id) {
        return supplier.get(id);
    }

    @Override
    public Set<Supplier> getAll() {
        return Set.copyOf(supplier.values());
    }
}
