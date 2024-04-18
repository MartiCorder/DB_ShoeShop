package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.Supplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierRepositoryTest {

    @Test
    void save() {
        var path = System.getProperty("user.dir") + "/src/test/resources/supplier.ser/";
        var repository = new SupplierRepository(path);
        var supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("MMA Supplier");
        supplier1.setPhone("685771025");
        repository.save(supplier1);

        repository.load();
    }

    @Test
    void update() {
        var path = System.getProperty("user.dir") + "/src/test/resources/supplier.ser/";
        var repository = new SupplierRepository(path);

        var supplier1 = new Supplier();
        supplier1.setId(3);
        supplier1.setName("MRA Supplier");
        repository.save(supplier1);

        var updateSupplier = repository.get(1);
        assertNotNull(updateSupplier);
    }

    @Test
    void get() {
        var path = System.getProperty("user.dir") + "/src/test/resources/supplier.ser/";
        var repository = new SupplierRepository(path);

        var supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("Supplier 1");
        supplier1.setPhone("123456789");
        repository.save(supplier1);

        var retrievedSupplier = repository.get(1);
        assertNotNull(retrievedSupplier);
        assertEquals(1, retrievedSupplier.getId());
        assertEquals("Supplier 1", retrievedSupplier.getName());
        assertEquals("123456789", retrievedSupplier.getPhone());
    }

    @Test
    void getAll() {
        var path = System.getProperty("user.dir") + "/src/test/resources/supplier.ser/";
        var repository = new SupplierRepository(path);

        var supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("Supplier 1");
        supplier1.setPhone("123456789");
        repository.save(supplier1);

        var supplier2 = new Supplier();
        supplier2.setId(2);
        supplier2.setName("Supplier 2");
        supplier2.setPhone("987654321");
        repository.save(supplier2);

        var allSuppliers = repository.getAll();
        assertNotNull(allSuppliers);
        assertEquals(2, allSuppliers.size());
    }
}
