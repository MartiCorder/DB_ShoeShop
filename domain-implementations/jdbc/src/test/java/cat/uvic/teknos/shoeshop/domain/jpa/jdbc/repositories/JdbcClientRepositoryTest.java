package cat.uvic.teknos.shoeshop.domain.jpa.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.*;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcClientRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcClientRepositoryTest {

    private final Connection connection;

    public JdbcClientRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    void shouldInsertClient() {

        Address address = new Address();
        address.setLocation("Carrer Vic 2");

        ShoeStore shoeStore1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
        shoeStore1.setId(1);//Al haver-hi això el relacionarà amb la ShoeStore 1

        Client client = new Client();
        client.setDni("47688994G");
        client.setName("Joel");
        client.setPhone("766654735");
        client.setAddresses(address);
        client.setShoeStores(shoeStore1);

        var repository = new JdbcClientRepository(connection);
        repository.save(client);

        assertTrue(client.getId() > 0);
        assertTrue(client.getAddresses().getId() > 0);

        DbAssertions.assertThat(connection)
                .table("CLIENT")
                .where("CLIENT_ID", client.getId())
                .hasOneLine();

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ADDRESS_ID", client.getAddresses().getId())
                .hasOneLine();


    }

    @Test
    void shouldUpdateClient() {
        Client client = new Client();
        client.setId(1);
        client.setDni("87654321X");
        client.setName("Joel");
        client.setPhone("987654321");

        Address address = new Address();
        address.setId(1);
        address.setLocation("456 Elm St");

        client.setAddresses(address);

        var repository = new JdbcClientRepository(connection);
        repository.save(client);

        DbAssertions.assertThat(connection)
                .table("CLIENT")
                .where("CLIENT_ID", client.getId())
                .hasOneLine();
    }
    @Test
    void delete() {
        Client client = new Client();
        client.setId(3);

        var repository = new JdbcClientRepository(connection);
        repository.delete(client);


    }

    @Test
    void get() {
        int id = 1;
        var repository = new JdbcClientRepository(connection);

        Client client = (Client) repository.get(id);
        printClient(client);
    }

    @Test
    void getAll() {
        var repository = new JdbcClientRepository(connection);
        Set<cat.uvic.teknos.shoeshop.models.Client> clients = repository.getAll();
        assertNotNull(clients);

        for (var client : clients) {
            printClient((Client) client);
        }
    }

    private static void printClient(Client client) {
        System.out.println("Client ID: " + client.getId());
        System.out.println("DNI: " + client.getDni());
        System.out.println("Name: " + client.getName());
        System.out.println("Phone: " + client.getPhone());
        if (client.getAddresses() != null) {
            System.out.println("-----\nAddress");
            System.out.println("Location: " + client.getAddresses().getLocation());
        }
        System.out.println("\n\n");
    }
}
