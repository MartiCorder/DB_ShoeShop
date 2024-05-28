package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.util.Set;
import  cat.uvic.teknos.shoeshop.models.Shoe;
import  cat.uvic.teknos.shoeshop.models.ShoeStore;

@Entity
@Table(name = "INVENTORY")
public class Inventory implements cat.uvic.teknos.shoeshop.models.Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORY_ID")
    private int id;

    @Column(name = "CAPACITY")
    private int capacity;

    @OneToMany(mappedBy = "inventory")
    private Set<Shoe> shoes;

    @ManyToMany(mappedBy = "inventories")
    private Set<ShoeStore> shoeStores;

    public Inventory() {
    }

    public Inventory(int capacity) {
        this.capacity = capacity;
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
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public Set<cat.uvic.teknos.shoeshop.models.ShoeStore> getShoeStores() {
        return shoeStores;
    }

    @Override
    public void setShoeStores(Set<cat.uvic.teknos.shoeshop.models.ShoeStore> shoeStores) {
        this.shoeStores=shoeStores;
    }

    @Override
    public Set<cat.uvic.teknos.shoeshop.models.Shoe> getShoes() {
        return shoes;
    }

    @Override
    public void setShoes(Set<cat.uvic.teknos.shoeshop.models.Shoe> shoes) {
        this.shoes=shoes;
    }

}
