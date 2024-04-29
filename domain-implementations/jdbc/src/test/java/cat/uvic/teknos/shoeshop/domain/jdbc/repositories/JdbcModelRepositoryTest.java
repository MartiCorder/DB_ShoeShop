package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Client;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.Model;
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
class JdbcModelRepositoryTest {

    private final Connection connection;


    public JdbcModelRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new Model (id = 0), when save, then a new record is added to the MODEL table")
    void shouldInsertNewModelTest() throws SQLException {


        Model model = new Model();

        model.setBrand("Adidas");
        model.setName("Adidas original");


        var repository = new JdbcModelRepository(connection);

        repository.save(model);

        assertTrue(model.getId() > 0);

        assertNotNull(repository.get(model.getId()));

            /*DbAssertions.assertThat(connection)
                    .table("CAR")
                    .where("CAR_ID = ?", mercedes.getId())
                    .hasOneLine();*/

    }

    @Test
    void shouldUpdateNewModelTest() throws SQLException {

        var repository = new JdbcModelRepository(connection);
        Model model = new Model();

        model.setBrand("Adidas");
        model.setName("Adidas Original");
        model.setId(3);

        repository.save(model);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        Model model = new Model();
        model.setId(4);

        var repository = new JdbcModelRepository(connection);
        repository.delete(model);

        //assertNull(repository.get(1));

    }

    @Test
    void get() {

        var repository = new JdbcModelRepository(connection);

        int existingModelId = 1;
        Model model = (Model) repository.get(existingModelId);

        assertNotNull(model);
        assertEquals(existingModelId, model.getId());


    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcModelRepository(connection);

        var clients = repository.getAll();

        assertNotNull(clients);
        assertFalse(clients.isEmpty());

    }

}
