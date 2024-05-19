package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

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

    @ManyToMany(mappedBy = "shoeStores")
    private Set<Client> clients;

    @ManyToMany
    @JoinTable(
            name = "SHOE_STORE_INVENTORY",
            joinColumns = @JoinColumn(name = "SHOE_STORE_ID"),
            inverseJoinColumns = @JoinColumn(name = "INVENTORY_ID")
    )
    private Set<Inventory> inventories;

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

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        this.inventories = inventories;
    }
}
