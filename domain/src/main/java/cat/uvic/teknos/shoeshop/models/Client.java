package cat.uvic.teknos.shoeshop.models;

public interface Client {

    int getId();
    void setId(int id);

    String getDni();
    void setDni(String dni);

    String getName();
    void setName(String name);

    String getPhone();
    void setPhone(String phone);

}
