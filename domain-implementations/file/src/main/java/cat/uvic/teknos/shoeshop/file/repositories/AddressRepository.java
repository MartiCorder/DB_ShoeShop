package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class AddressRepository implements cat.uvic.teknos.shoeshop.repositories.AddressRepository{

    private Map<Integer, Address> address = new HashMap<>();
    private final String path;
    public AddressRepository(String path){

        this.path=path;

        load();
    }

    public void load(){

        //var currentDirectory = System.getProperty("user.dir") + "/src/main/resources/";

        try(var inputStream = new ObjectInputStream(new
                FileInputStream(path))) {
            address = (Map<Integer, Address>)
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
            outputStream.writeObject(address);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Address model) {
        if (model.getId() <= 0){
            //get new id
            var newId=address.keySet().stream().mapToInt(k ->
                    k).max().orElse(0)+1;
            address.put(newId, model);
        }else{
            if (address.get(model.getId())==null){
                throw new RuntimeException("Team with id"+
                        model.getId()+"Not found");
            }
            address.put(model.getId(), model);
        }
        write();
    }

    public void update(){
        //var currentDirectory = System.getProperty("user.dir")+ "/src/main/resources/";
        try (var outputStream = new ObjectOutputStream(new
                FileOutputStream(path))) {
            outputStream.writeObject(address);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Address model) {
        address.remove(model.getId());
    }

    public Address get(Integer id) {
        return address.get(id);
    }

    @Override
    public Set<Address> getAll() {
        return Set.copyOf(address.values());
    }
}
