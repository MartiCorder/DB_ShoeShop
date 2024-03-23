package cat.uvic.teknos.shoeshop.file.models;

public class Model implements cat.uvic.teknos.shoeshop.models.Model {
    private int id;
    private String name;

    private String brand;
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
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand=brand;
    }
}
