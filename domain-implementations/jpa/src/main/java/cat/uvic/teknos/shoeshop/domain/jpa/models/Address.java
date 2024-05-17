package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ADDRESS")
public class Address implements cat.uvic.teknos.shoeshop.models.Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ADDRESS")
    private int id;

    @Column(name = "LOCATION")
    private String location;

    @OneToMany(mappedBy = "address")
    private List<Client> clients;

    public Address() {
    }

    public Address(String location) {
        this.location = location;
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
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }
}
