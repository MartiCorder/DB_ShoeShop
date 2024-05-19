package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CLIENT")
public class Client implements cat.uvic.teknos.shoeshop.models.Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENT")
    private int id;

    @Column(name = "DNI")
    private String dni;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "ID_ADDRESS")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "CLIENT_SHOE_STORE",
            joinColumns = @JoinColumn(name = "CLIENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "SHOE_STORE_ID")
    )
    private Set<ShoeStore> shoeStores;

    public Client() {
    }

    public Client(String dni, String name, String phone) {
        this.dni = dni;
        this.name = name;
        this.phone = phone;
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
    public String getDni() {
        return dni;
    }

    @Override
    public void setDni(String dni) {
        this.dni = dni;
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
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<ShoeStore> getShoeStores() {
        return shoeStores;
    }

    public void setShoeStores(Set<ShoeStore> shoeStores) {
        this.shoeStores = shoeStores;
    }
}
