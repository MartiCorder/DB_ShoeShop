package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MODEL")
public class Model implements cat.uvic.teknos.shoeshop.models.Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MODEL_ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BRAND")
    private String brand;

    @OneToMany(mappedBy = "model")
    private Set<Shoe> shoes;

    public Model() {
    }

    public Model(String name, String brand) {
        this.name = name;
        this.brand = brand;
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
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
