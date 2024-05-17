package cat.uvic.teknos.shoeshop.domain.jpa.models;

import jakarta.persistence.*;


@Entity
@Table(name = "CLIENT")
public class Client implements cat.uvic.teknos.shoeshop.models.Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "DNI")
    private String dni;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @OneToOne
    @JoinColumn(name = "ID_ADDRESS")
    private Address address;

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
}
