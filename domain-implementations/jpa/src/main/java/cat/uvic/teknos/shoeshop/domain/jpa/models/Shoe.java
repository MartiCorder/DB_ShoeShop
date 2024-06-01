package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.Set;
import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.Inventory;

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

    @ManyToOne(targetEntity = cat.uvic.teknos.shoeshop.domain.jpa.models.Model.class)
    @JoinColumn(name = "MODEL_ID")
    private Model model;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = cat.uvic.teknos.shoeshop.domain.jpa.models.Inventory.class )
    @JoinTable(name = "SHOE_INVENTORY", joinColumns = @JoinColumn(name = "INVENTORY_ID"), inverseJoinColumns = @JoinColumn(name = "SHOE_ID"), uniqueConstraints = { @UniqueConstraint( columnNames = {"INVENTORY_ID", "SHOE_ID"})})
    @Transient

    private Inventory inventories;

    private Set<Inventory> inventories;


    @ManyToOne(targetEntity = cat.uvic.teknos.shoeshop.domain.jpa.models.Inventory.class )
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

    public Inventory getInventories() {

    public Set<Inventory> getInventories() {

        return inventories;
    }

    @Override

    public void setInventories(Inventory inventories) {

    public void setInventories(Set<Inventory> inventories) {

        this.inventories = inventories;
    }

    @Override
    public cat.uvic.teknos.shoeshop.models.Model getModels() {
        return model;
    }

    @Override
    public void setModels(cat.uvic.teknos.shoeshop.models.Model model) {
        this.model=model;
    }
}
