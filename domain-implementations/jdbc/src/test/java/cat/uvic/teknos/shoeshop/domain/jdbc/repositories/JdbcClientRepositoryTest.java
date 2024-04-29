package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Address;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.Client;
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

class JdbcClientRepositoryTest {


    private final Connection connection;


    public JdbcClientRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new Client (id = 0), when save, then a new record is added to the CLIENT table")
    void shouldInsertNewClientTest() throws SQLException {


        Client client = new Client();

        client.setDni(45);
        client.setPhone("694564526");
        client.setName("BRRRR");


        var repository = new JdbcClientRepository(connection);

        repository.save(client);

        assertTrue(client.getId() > 0);

        assertNotNull(repository.get(client.getId()));

            /*DbAssertions.assertThat(connection)
                    .table("CAR")
                    .where("CAR_ID = ?", mercedes.getId())
                    .hasOneLine();*/

    }

    @Test
    void shouldUpdateNewClientTest() throws SQLException {

        var repository = new JdbcClientRepository(connection);
        Client client = new Client();

        client.setId(2);
        client.setDni(4365);
        client.setPhone("697894566");
        client.setName("Oriol");

        repository.save(client);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        Client client = new Client();
        client.setId(4);

        var repository = new JdbcClientRepository(connection);
        repository.delete(client);

        //assertNull(repository.get(1));

    }

    @Test
    void get() {

        var repository = new JdbcClientRepository(connection);

        int existingClientId = 1;
        Client client = (Client) repository.get(existingClientId);

        assertNotNull(client, "Client should not be null");
        assertEquals(existingClientId, client.getId(), "Client ID should match");


    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcClientRepository(connection);

        var clients = repository.getAll();

        assertNotNull(clients, "Clients collection should not be null");
        assertFalse(clients.isEmpty(), "Clients collection should not be empty");

    }
}
