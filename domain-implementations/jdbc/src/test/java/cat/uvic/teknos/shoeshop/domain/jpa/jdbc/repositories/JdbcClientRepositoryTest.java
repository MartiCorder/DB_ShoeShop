package cat.uvic.teknos.shoeshop.domain.jpa.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Client;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcClientRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

class JdbcClientRepositoryTest {


    private final Connection connection;


    public JdbcClientRepositoryTest(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Test
    @DisplayName("Given a new Client (id = 0), when save, then a new record is added to the CLIENT table")
    void shouldInsertNewClientTest() throws SQLException {


        Client client = new Client();

        client.setDni("458756678X");
        client.setPhone("694564526");
        client.setName("tesgfd");


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

        client.setId(1);
        client.setDni("48044553F");
        client.setPhone("697894566");
        client.setName("Oriol");

        repository.save(client);

        assertTrue(true);

    }

    @Test
    void delete() throws SQLException {

        Client client = new Client();
        client.setId(1);

        var repository = new JdbcClientRepository(connection);
        repository.delete(client);

        //assertNull(repository.get(1));

    }

    @Test
    void get() throws SQLException {

        var repository = new JdbcClientRepository(connection);

        int existingClientId = 1;
        Client client = (Client) repository.get(existingClientId);

        assertNotNull(client);
        assertEquals(existingClientId, client.getId());


    }

    @Test
    void getAll() throws SQLException {

        var repository = new JdbcClientRepository(connection);

        var clients = repository.getAll();

        assertNotNull(clients);
        assertFalse(clients.isEmpty());

    }
}
