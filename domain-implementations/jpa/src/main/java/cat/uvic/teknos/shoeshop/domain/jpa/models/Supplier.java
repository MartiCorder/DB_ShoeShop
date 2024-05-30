package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
@Entity
@Table(name = "SUPPLIER")
public class Supplier implements cat.uvic.teknos.shoeshop.models.Supplier, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUPPLIER_ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @ManyToMany(mappedBy = "suppliers", targetEntity = cat.uvic.teknos.shoeshop.domain.jpa.models.ShoeStore.class)
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

    @Override
    public Set<cat.uvic.teknos.shoeshop.models.ShoeStore> getShoeStores() {
        return shoeStores;
    }


    @Override
    public void setShoeStores(Set<cat.uvic.teknos.shoeshop.models.ShoeStore> shoeStores) {
        this.shoeStores=shoeStores;
    }
}
