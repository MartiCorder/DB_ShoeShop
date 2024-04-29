package cat.uvic.teknos.shoeshop.models;

public interface Client {

    int getId();
    void setId(int id);

    int getDni();
    void setDni(int dni);

    String getName();
    void setName(String name);

    String getPhone();
    void setPhone(String phone);

}
