package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.Supplier;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SupplierRepository implements cat.uvic.teknos.shoeshop.repositories.SupplierRepository, Serializable{

    private static Map<Integer, Supplier> supplier = new HashMap<>();

    public static void load(){

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";


        try(var inputStream = new ObjectInputStream(new FileInputStream(currentDirectory+ "supplier.ser"))) {
            supplier = (Map<Integer, Supplier>) inputStream.readObject();
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

        try(var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "supplier.ser"))) {
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
        write();

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

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "supplier.ser"))) {

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
