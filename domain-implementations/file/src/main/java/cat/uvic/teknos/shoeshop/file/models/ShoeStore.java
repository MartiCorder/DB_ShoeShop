package cat.uvic.teknos.shoeshop.file.models;

public class ShoeStore implements cat.uvic.teknos.shoeshop.models.ShoeStore {

    private int id;
    private String name;

    private String owner;

    private String location;

    private int inventoryId;
    @Override


    public int getId() {

        return id;
    }

    @Override
    public void setId(int id) {

        this.id=id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

        this.name=name;

    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner=owner;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location=location;
    }

    @Override
    public int getInventoryId() {
        return inventoryId;
    }

    @Override
    public void setInventoryId(int id_inventory) {

        this.inventoryId=id_inventory;

    }
}
