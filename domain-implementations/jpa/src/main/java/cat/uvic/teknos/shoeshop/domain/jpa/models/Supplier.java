package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SUPPLIER")
public class Supplier implements cat.uvic.teknos.shoeshop.models.Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @OneToMany(mappedBy = "supplier")
    private List<ShoeStore> shoeStores;


    public Supplier() {
    }

    public Supplier(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
