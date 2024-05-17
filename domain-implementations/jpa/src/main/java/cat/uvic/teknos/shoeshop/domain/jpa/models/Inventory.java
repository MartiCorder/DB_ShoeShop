package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "INVENTORY")
public class Inventory implements cat.uvic.teknos.shoeshop.models.Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CAPACITY")
    private int capacity;

    @OneToMany(mappedBy = "inventory")
    private List<Shoe> shoes;

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
}
