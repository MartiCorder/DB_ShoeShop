package cat.uvic.teknos.shoeshop.domain.jpa.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcClientRepository;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcShoeStoreRepository;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Supplier;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcShoeStoreRepositoryTest {

    private final Connection connection;

    public JdbcShoeStoreRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    public void shouldInsert() throws SQLException {

        ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
        shoeStore.setId(1);
        shoeStore.setName("Test Store");
        shoeStore.setOwner("Test Owner");
        shoeStore.setLocation("Test Location");

        Inventory inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
        inventory.setId(1);
        inventory.setCapacity(100);

        Supplier supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
        supplier.setId(1);
        supplier.setName("Test Supplier");
        supplier.setPhone("123456789");

        Set<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);
        shoeStore.setInventories(inventories);

        Set<Supplier> suppliers = new HashSet<>();
        suppliers.add(supplier);
        shoeStore.setSuppliers(suppliers);

        var repository = new JdbcShoeStoreRepository(connection);

        repository.save(shoeStore);

    }

    @Test
    public void testDeleteShoeStore() throws SQLException {

        ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
        shoeStore.setId(1);


        var repository = new JdbcShoeStoreRepository(connection);

        repository.delete(shoeStore);

    }

    @Test
    public void testGetAllShoeStores() throws SQLException {

    }
}
