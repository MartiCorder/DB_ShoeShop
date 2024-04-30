package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Client;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcShoeStoreRepositoryTest {

    private final Connection connection;


    public JdbcShoeStoreRepositoryTest(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Test
    @DisplayName("Given a new ShoeStore (id = 0), when save, then a new record is added to the SHOESTORE table")
    void shouldInsertNewShoeStoreTest() throws SQLException {


        ShoeStore shoeStore = new ShoeStore();

        shoeStore.setName("Taradell's Shop");
        shoeStore.setOwner("Corder");
        shoeStore.setLocation("Taradell");
        shoeStore.setInventoryId(2);


        var repository = new JdbcShoeStoreRepository(connection);

        repository.save(shoeStore);

        assertTrue(shoeStore.getId() > 0);

        assertNotNull(repository.get(shoeStore.getId()));

            /*DbAssertions.assertThat(connection)
                    .table("CAR")
                    .where("CAR_ID = ?", mercedes.getId())
                    .hasOneLine();*/

    }

    @Test
    void shouldUpdateNewClientTest() throws SQLException {

        var repository = new JdbcShoeStoreRepository(connection);
        ShoeStore shoeStore = new ShoeStore();

        shoeStore.setId(1);
        shoeStore.setName("Taradell's Shop");
        shoeStore.setOwner("Corder");
        shoeStore.setLocation("Taradell");
        shoeStore.setInventoryId(2);

        repository.save(shoeStore);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        ShoeStore shoeStore = new ShoeStore();
        shoeStore.setId(2);

        var repository = new JdbcShoeStoreRepository(connection);
        repository.delete(shoeStore);

        //assertNull(repository.get(1));

    }

    @Test
    void get() throws SQLException {

        var repository = new JdbcShoeStoreRepository(connection);

        int existingShoeStoreId = 1;
        ShoeStore shoestore = (ShoeStore) repository.get(existingShoeStoreId);

        assertNotNull(shoestore);
        assertEquals(existingShoeStoreId, shoestore.getId());

    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcShoeStoreRepository(connection);

        var shoeStores = repository.getAll();

        assertNotNull(shoeStores);
        assertFalse(shoeStores.isEmpty());

    }
}
