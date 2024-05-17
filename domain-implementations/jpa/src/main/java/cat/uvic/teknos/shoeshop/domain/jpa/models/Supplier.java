package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "SUPPLIER")
public class Supplier implements cat.uvic.teknos.shoeshop.models.Supplier, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUPPLIER")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShoeStore> shoeStores;

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

    public Set<ShoeStore> getShoeStores() {
        return shoeStores;
    }

    public void setShoeStores(Set<ShoeStore> shoeStores) {
        this.shoeStores = shoeStores;
    }
}
