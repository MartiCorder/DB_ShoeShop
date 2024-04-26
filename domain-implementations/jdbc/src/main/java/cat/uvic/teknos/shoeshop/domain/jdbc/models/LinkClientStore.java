package cat.uvic.teknos.shoeshop.domain.jdbc.models;
import java.io.Serializable;

public class LinkClientStore implements cat.uvic.teknos.shoeshop.models.LinkClientStore, Serializable {

    private String dni;

    private int id;

    @Override
    public String getClientDni() {
        return dni;
    }

    @Override
    public void setClientDni(String dni) {

        this.dni=dni;

    }

    @Override
    public int getStoreId() {
        return id;
    }

    @Override
    public void setStoreId(int id) {

        this.id=id;

    }
}
