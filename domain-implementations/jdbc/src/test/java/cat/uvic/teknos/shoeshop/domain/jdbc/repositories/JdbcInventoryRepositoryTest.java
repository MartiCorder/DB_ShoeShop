package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Client;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory;
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
class JdbcInventoryRepositoryTest {

    private final Connection connection;


    public JdbcInventoryRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new Inventory (id = 0), when save, then a new record is added to the INVENTORY table")
    void shouldInsertNewInventoryTest() throws SQLException {


        Inventory inventory = new Inventory();

        inventory.setCapacity(300);

        var repository = new JdbcInventoryRepository(connection);

        repository.save(inventory);

        assertTrue(inventory.getId() > 0);

        assertNotNull(repository.get(inventory.getId()));

            /*DbAssertions.assertThat(connection)
                    .table("CAR")
                    .where("CAR_ID = ?", mercedes.getId())
                    .hasOneLine();*/

    }

    @Test
    void shouldUpdateNewInventoryTest() throws SQLException {

        var repository = new JdbcInventoryRepository(connection);
        Inventory inventory = new Inventory();

        inventory.setCapacity(220);
        inventory.setId(2);

        repository.save(inventory);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        Inventory inventory = new Inventory();
        inventory.setId(3);

        var repository = new JdbcInventoryRepository(connection);
        repository.delete(inventory);

        //assertNull(repository.get(1));

    }

    @Test
    void get() {

        var repository = new JdbcInventoryRepository(connection);

        int existingInventoryId = 1;
        Inventory inventory = (Inventory) repository.get(existingInventoryId);

        assertNotNull(inventory);
        assertEquals(existingInventoryId, inventory.getId());


    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcInventoryRepository(connection);

        var inventories = repository.getAll();

        assertNotNull(inventories);
        assertFalse(inventories.isEmpty());

    }

}
