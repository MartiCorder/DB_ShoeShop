package cat.uvic.teknos.shoeshop.domain.jdbc.models;

import java.io.Serializable;

public class Address implements cat.uvic.teknos.shoeshop.models.Address, Serializable{

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
