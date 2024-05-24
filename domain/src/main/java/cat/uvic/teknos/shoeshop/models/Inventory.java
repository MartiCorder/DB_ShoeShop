package cat.uvic.teknos.shoeshop.models;

import java.util.Set;

public interface Inventory {

    int getId();

    void setId(int id);

    int getCapacity();

    void setCapacity(int capacity);

    Set<ShoeStore> getShoeStores();
    void setShoeStores(Set<ShoeStore> shoeStores);

    Set<Shoe> getShoes();
    void setShoes(Set<Shoe> shoes);
}
