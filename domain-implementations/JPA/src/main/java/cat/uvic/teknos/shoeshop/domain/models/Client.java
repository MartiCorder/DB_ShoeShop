package cat.uvic.teknos.shoeshop.domain.models;
import java.io.Serializable;

public class Client implements cat.uvic.teknos.shoeshop.models.Client, Serializable{

    private int id;
    private String dni;
    private String name;
    private String phone;
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {

        this.id=id;

    }

    @Override
    public String getDni() {
        return dni;
    }

    @Override
    public void setDni(String dni) {

        this.dni=dni;

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
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone=phone;
    }
}
