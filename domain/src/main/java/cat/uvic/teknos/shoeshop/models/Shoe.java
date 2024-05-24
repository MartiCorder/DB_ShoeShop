package cat.uvic.teknos.shoeshop.models;

import java.util.Set;

public interface Shoe {

    int getId();

    void setId(int id_shoe);


    double getPrice();

    void setPrice(double price);

    String getColor();

    void setColor(String color);

    String getSize();

    void setSize(String size);

    Set<Inventory> getInventories();
    void setInventories(Set<Inventory> inventories);

    Model getModels();
    void setModels(Model models);


}
