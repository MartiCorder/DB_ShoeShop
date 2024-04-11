package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class SupplierRepository implements cat.uvic.teknos.shoeshop.repositories.SupplierRepository{

    private static Map<Integer, Supplier> supplier;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            supplier = (Map<Integer, Supplier>) inputStream.readObject();
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
            //get new id
            var newId=supplier.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            supplier.put(newId, model);
        }else{
            supplier.put(model.getId(), model);
        }
    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "supplier.ser"))) {
            outputStream.writeObject(supplier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Supplier model) {
        supplier.remove(model.getId());
    }

    @Override
    public Supplier get(Integer id) {
        return null;
    }

    @Override
    public Set<Supplier> getAll() {
        return null;
    }
}
