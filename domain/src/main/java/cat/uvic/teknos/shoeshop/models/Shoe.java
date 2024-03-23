package cat.uvic.teknos.shoeshop.models;

public interface Shoe {

    int getId();

    void setId(int id_shoe);

    String getName();

    void setName(String name);

    int getModelId();

    void setModelId(int id_model);

    int getInventoryId();

    void setInventoryId(int id_inventory);
    double getPrice();

    void setPrice(double price);

    String getColor();

    void setColor(String color);

    String getSize();

    void setSize(String size);


}
