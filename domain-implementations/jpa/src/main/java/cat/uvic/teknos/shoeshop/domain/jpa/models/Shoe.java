package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

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
    @JoinColumn(name = "INVENTPRY_ID")
    private Inventory inventory;

    public Shoe() {
    }

    public Shoe(Model model, Inventory inventory, double price, String color, String size) {
        this.model = model;
        this.inventory = inventory;
        this.price = price;
        this.color = color;
        this.size = size;
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
    public int getModelId() {
        return model != null ? model.getId() : 0;
    }

    @Override
    public void setModelId(int modelId) {
        if (this.model == null) {
            this.model = new Model();
        }
        this.model.setId(modelId);
    }

    @Override
    public int getInventoryId() {
        return inventory != null ? inventory.getId() : 0;
    }

    @Override
    public void setInventoryId(int inventoryId) {
        if (this.inventory == null) {
            this.inventory = new Inventory();
        }
        this.inventory.setId(inventoryId);
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
}
