package cat.uvic.teknos.shoeshop.models;

import java.util.Set;

public interface ShoeStore {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getOwner();

    void setOwner(String owner);

    String getLocation();

    void setLocation(String location);

    Set<Supplier> getSuppliers();
    void setSuppliers(Set<Supplier> suppliers);

    Set<Client> getClients();
    void setClients(Set<Client> clients);

    Set<Inventory> getInventories();
    void setInventories(Set<Inventory> inventories);

}
