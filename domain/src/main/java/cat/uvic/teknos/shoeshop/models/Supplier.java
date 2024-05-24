package cat.uvic.teknos.shoeshop.models;

import java.util.Set;

public interface Supplier {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getPhone();

    void setPhone(String phone);

    Set<ShoeStore> getShoeStores();
    void setShoeStores(Set<ShoeStore> shoeStores);

}
