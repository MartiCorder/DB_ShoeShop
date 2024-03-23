package cat.uvic.teknos.shoeshop.file.models;

public class Inventory implements cat.uvic.teknos.shoeshop.models.Inventory{

    private int id;
    private  int capacity;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity=capacity;
    }
}
