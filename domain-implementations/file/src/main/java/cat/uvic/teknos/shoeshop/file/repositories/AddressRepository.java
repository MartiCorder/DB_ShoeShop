package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Address;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class AddressRepository implements cat.uvic.teknos.shoeshop.repositories.AddressRepository{

    private static Map<Integer, Address> address;

    public static void load(){

        try(var inputStream = new ObjectInputStream(new FileInputStream(""))) {
            address = (Map<Integer, Address>) inputStream.readObject();
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
        address.remove(model.getId());

    }

    @Override
    public Address get(Integer id) {
        return null;
    }

    @Override
    public Set<Address> getAll() {
        return null;
    }
}
