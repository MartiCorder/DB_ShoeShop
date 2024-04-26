package cat.uvic.teknos.shoeshop.domain.jdbc.models;
import java.io.Serializable;
public class Supplier implements cat.uvic.teknos.shoeshop.models.Supplier, Serializable{

    private int id;
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
