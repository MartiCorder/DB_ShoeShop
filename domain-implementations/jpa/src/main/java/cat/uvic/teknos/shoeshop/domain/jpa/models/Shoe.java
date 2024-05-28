package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SHOE")
public class Shoe implements cat.uvic.teknos.shoeshop.models.Shoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOE_ID")
    private int id;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @ManyToOne
    @JoinColumn(name = "MODEL_ID")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "INVENTORY_ID")
    private Inventory inventory;

    public Shoe() {
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
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public Set<cat.uvic.teknos.shoeshop.models.Inventory> getInventories() {
        return (Set<cat.uvic.teknos.shoeshop.models.Inventory>) inventory;
    }

    @Override
    public void setInventories(Set<cat.uvic.teknos.shoeshop.models.Inventory> inventories) {
        this.inventory=inventory;
    }

    @Override
    public cat.uvic.teknos.shoeshop.models.Model getModels() {
        return model;
    }

    @Override
    public void setModels(cat.uvic.teknos.shoeshop.models.Model models) {
        this.model=model;
    }
}
