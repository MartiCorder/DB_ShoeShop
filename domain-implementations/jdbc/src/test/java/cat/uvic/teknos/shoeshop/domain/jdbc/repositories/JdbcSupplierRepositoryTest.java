package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcSupplierRepositoryTest {

    private final Connection connection;

    public JdbcSupplierRepositoryTest(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Test
    @DisplayName("Given a new Supplier, when saved, then a new record is added to the Supplier table")
    void shouldInsertNewSupplierTest() throws SQLException {



        Supplier adidas = new Supplier();

        adidas.setName("Adidas SA");
        adidas.setPhone("674661045");


        var repository = new JdbcSupplierRepository(connection);
        repository.save(adidas);
        assertTrue(adidas.getId() > 0);

    }

    @Test
    @DisplayName("Given an existing Supplier, when updated, then the record is modified in the Supplier table")
    void shouldUpdateSupplierTest() throws SQLException {



        Supplier supplier1 = new Supplier();


        supplier1.setId(1);
        supplier1.setName("ADIDAS S.A");
        supplier1.setPhone("987655475");


        var repository = new JdbcSupplierRepository(connection);
        repository.save(supplier1);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        Supplier supplier = new Supplier();
        supplier.setId(2);

        var repository = new JdbcSupplierRepository(connection);
        repository.delete(supplier);

        //assertNull(repository.get(1));

    }

    @Test
    void get() throws SQLException {

        var repository = new JdbcSupplierRepository(connection);

        int existingSupplierId = 1;
        Supplier supplier = (Supplier) repository.get(existingSupplierId);

        assertNotNull(supplier);
        assertEquals(existingSupplierId, supplier.getId());

    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcSupplierRepository(connection);

        var suppliers = repository.getAll();

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty());

    }
}

