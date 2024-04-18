package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.*;

public class AddressRepository implements cat.uvic.teknos.shoeshop.repositories.AddressRepository{

    private static Map<Integer, Address> address = new HashMap<>();

    public static void load(){

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";


        try(var inputStream = new ObjectInputStream(new FileInputStream(currentDirectory+ "address.ser"))) {
            address = (Map<Integer, Address>) inputStream.readObject();
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

        try(var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "address.ser"))) {
            outputStream.writeObject(address);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Address model) {
        if (model.getId() <= 0){
            //get new id
            var newId=address.keySet().stream().mapToInt(k -> k).max().orElse(0)+1;
            address.put(newId, model);
        }else{
            address.put(model.getId(), model);
        }
        write();

    }
    public static void update(){
        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "address.ser"))) {
            outputStream.writeObject(address);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Address model) {

        var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try (var outputStream = new ObjectOutputStream(new FileOutputStream(currentDirectory + "address.ser"))) {

            for (Iterator<Map.Entry<Integer, Address>> iterator = address.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Integer, Address> entry = iterator.next();
                if (entry.getValue().equals(model)) {
                    iterator.remove();
                    break;
                }
            }

            outputStream.writeObject(address);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Address get(Integer id) {
        return address.get(id);
    }

    @Override
    public Set<Address> getAll() {
        return Set.copyOf(address.values());
    }
}
