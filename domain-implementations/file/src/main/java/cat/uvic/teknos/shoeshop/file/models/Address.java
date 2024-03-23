package cat.uvic.teknos.shoeshop.file.models;

public class Address implements cat.uvic.teknos.shoeshop.models.Address{

    private int id;
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {

        this.id=id;

    }
}
