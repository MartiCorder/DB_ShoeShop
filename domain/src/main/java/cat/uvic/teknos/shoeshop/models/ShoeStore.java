package cat.uvic.teknos.shoeshop.models;

public interface ShoeStore {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getOwner();

    void setOwner(String owner);

    String getLocation();

    void setLocation(String location);

    int getInventoryId();

    void setInventoryId(int id_inventory);

}
