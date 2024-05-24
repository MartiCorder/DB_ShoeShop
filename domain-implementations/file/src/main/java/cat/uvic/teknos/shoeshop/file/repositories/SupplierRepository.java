package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;

import java.io.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SupplierRepository implements cat.uvic.teknos.shoeshop.repositories.SupplierRepository, Serializable{

    private static Map<Integer, Supplier> suppliers = new HashMap<>();

    private final String path;

    public SupplierRepository(String path){
        this.path=path;
        load();
    }

    void load(){
        var file = new File(path);
        if(file.exists()) {
            try (var inputStream = new ObjectInputStream(new FileInputStream(path))) {
                suppliers = (Map<Integer, Supplier>) inputStream.readObject();
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
    public void save(Supplier model) {
        if(model.getId() <= 0){
            var newId = suppliers.keySet().stream().mapToInt(k -> k).max().orElse(0) + 1;
            model.setId(newId);
            suppliers.put(newId, model);
        }else{
            suppliers.put(model.getId(), model);
        }
        write();
    }
    @Override
    public void delete(Supplier model) {

        suppliers.remove(model.getId());
        write();
    }

    @Override
    public Supplier get(Integer id) {
        return suppliers.get(id);
    }

    @Override
    public Set<Supplier> getAll() {
        return Set.copyOf(suppliers.values());
    }
}
