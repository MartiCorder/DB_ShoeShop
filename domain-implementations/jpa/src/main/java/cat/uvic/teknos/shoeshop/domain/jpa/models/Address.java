package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class Address implements cat.uvic.teknos.shoeshop.models.Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private int id;

    @Column(name = "LOCATION")
    private String location;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Client client;

    public Address() {
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
