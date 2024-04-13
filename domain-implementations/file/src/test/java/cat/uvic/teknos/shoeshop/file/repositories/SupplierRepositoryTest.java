package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.Supplier;
import org.junit.jupiter.api.Test;

import javax.naming.Name;
import java.util.jar.Attributes;
import static org.junit.jupiter.api.Assertions.*;

public class SupplierRepositoryTest {

    @Test
    void save() {
        var path = System.getProperty("user.dir") +
                "/src/main/resources/supplier.ser/";
        var repository = new SupplierRepository(path);
        var supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("MMA Supplier");
        supplier1.setPhone("685771025");
        repository.save(supplier1);

        assertTrue(supplier1.getId() > 0);
        assertNotNull(repository.get(supplier1.getId()));
        repository.load();
        assertNotNull(repository.get(supplier1.getId()));
    }

    @Test
    void update() {
        var path = System.getProperty("user.dir") +
                "/src/main/resources/supplier.ser/";
        var repository = new SupplierRepository(path);

        var supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("MRA Supplier");
        repository.save(supplier1);
        assertNotNull(repository.get(1));
        var updateSupplier = repository.get(1);
        assertEquals(1,updateSupplier.getId());
    }
}
