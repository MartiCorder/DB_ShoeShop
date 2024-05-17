package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SHOE_STORE")
public class ShoeStore implements cat.uvic.teknos.shoeshop.models.ShoeStore, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STORE")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "LOCATION")
    private String location;

    @ManyToOne
    @JoinColumn(name = "ID_SUPPLIER")
    private Supplier supplier;

    @Column(name = "ID_INVENTORY")
    private int inventoryId;

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
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int getInventoryId() {
        return inventoryId;
    }

    @Override
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
