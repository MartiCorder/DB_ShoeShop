package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SHOE_STORE")
public class ShoeStore implements cat.uvic.teknos.shoeshop.models.ShoeStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "ID_INVENTORY")
    private int inventoryId;

    @OneToOne
    @JoinColumn(name = "ID_INVENTORY")
    private Inventory inventory;
    public ShoeStore() {
    }

    public ShoeStore(String name, String owner, String location, int inventoryId) {
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.inventoryId = inventoryId;
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
}
