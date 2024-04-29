package cat.uvic.teknos.shoeshop.file.models;

import java.io.Serializable;

public class Shoe implements cat.uvic.teknos.shoeshop.models.Shoe, Serializable {

    private int id;

    private int modelId;

    private  int inventoryId;

    private double price;

    private String color;

    private String size;
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {

        this.id=id;

    }


    @Override
    public int getModelId() {
        return modelId;
    }

    @Override
    public void setModelId(int id_model) {

        this.modelId=id_model;
    }

    @Override
    public int getInventoryId() {
        return inventoryId;
    }

    @Override
    public void setInventoryId(int id_inventory) {

        this.inventoryId=id_inventory;

    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {

        this.price=price;

    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color=color;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public void setSize(String size) {

        this.size=size;

    }
}
