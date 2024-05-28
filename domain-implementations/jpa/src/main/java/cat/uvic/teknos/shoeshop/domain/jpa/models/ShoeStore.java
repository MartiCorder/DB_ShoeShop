package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;
import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.Inventory;
@Entity
@Table(name = "SHOE_STORE")
public class ShoeStore implements cat.uvic.teknos.shoeshop.models.ShoeStore, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "LOCATION")
    private String location;

    @OneToMany(mappedBy = "shoeStores")
    private Set<Client> clients;

    @ManyToMany
    @JoinTable(
            name = "SHOE_STORE_SUPPLIER",
            joinColumns = @JoinColumn(name = "SHOE_STORE_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUPPLIER_ID")
    )
    private Set<Supplier> suppliers;

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


    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    @Override
    public void setSuppliers(Set<cat.uvic.teknos.shoeshop.models.Supplier> suppliers) {
        this.suppliers=suppliers;
    }

    public Set<Client> getClients() {
        return clients;
    }

    @Override
    public void setClients(Set<cat.uvic.teknos.shoeshop.models.Client> clients) {
        this.clients=clients;
    }


    public Set<Inventory> getInventories() {
        return inventories;
    }

    @Override
    public void setInventories(Set<cat.uvic.teknos.shoeshop.models.Inventory> inventories) {
        this.inventories=inventories;
    }

}