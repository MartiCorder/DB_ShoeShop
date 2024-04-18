package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.Supplier;
import org.junit.jupiter.api.Test;
import java.io.Serializable;
import javax.naming.Name;
import java.util.jar.Attributes;
import static org.junit.jupiter.api.Assertions.*;

class SupplierRepositoryTest {
    @Test
    void save() {
        var path = System.getProperty("user.dir") +
                "/src/main/resources/supplier.ser/";
        var repository = new SupplierRepository();
        var supplier1 = new Supplier();
        //supplier1.setId(1);
        supplier1.setName("MMA Supplier");
        supplier1.setPhone("685771025");
        repository.save(supplier1);

        repository.load();

    }
    @Test
    void update() {
        var path = System.getProperty("user.dir") +
                "/src/main/resources/supplier.ser/";
        var repository = new SupplierRepository();

        var supplier1 = new Supplier();
        supplier1.setId(3);
        supplier1.setName("MRA Supplier");
        repository.save(supplier1);

        var updateSupplier = repository.get(1);
        
    }
}
