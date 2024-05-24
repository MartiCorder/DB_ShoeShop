package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Address;

import java.io.*;
import java.util.*;

public class AddressRepository implements cat.uvic.teknos.shoeshop.repositories.AddressRepository{

    private static Map<Integer, Address> addresses = new HashMap<>();

    private final String path;

    public AddressRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                addresses = (Map<Integer, Address>) inputStream.readObject();
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
    public void save(Address model) {
        if(model.getId() <= 0){
            var newId = addresses.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            addresses.put(newId, model);
        }else{
            addresses.put(model.getId(), model);
        }
        write();
    }

    @Override
    public void delete(Address model) {

        addresses.remove(model.getId());
        write();
    }

    @Override
    public Address get(Integer id) {
        return addresses.get(id);
    }

    @Override
    public Set<Address> getAll() {
        return Set.copyOf(addresses.values());
    }
}
