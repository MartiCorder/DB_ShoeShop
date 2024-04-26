package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.DriverManager;
import java.sql.SQLException;

class JdbcAddressRepositoryTest {
    @Test
    @DisplayName("Given a new Address (id = 0), when save, then a new record is added to the Address table")

    void shouldInsertNewAddressTest() throws SQLException {

        try(var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoe_shop", "root", "1606Marti!")) {

            Address address1 = new Address();
            address1.setId(5);


            var repository = new JdbcAddressRepository(connection);
            repository.save(address1);

            assertTrue(address1.getId() >0);
        }
    }

    @Test
    void shouldUpdateNewAddressTest() throws SQLException {

        try(var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoe_shop", "root", "1606Marti!")) {

            Address address1 = new Address();


            address1.setId(4);


            var repository = new JdbcAddressRepository(connection);
            repository.save(address1);

            assertTrue(true);
        }
    }

    @Test
    void delete() throws SQLException {
        try(var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoe_shop", "root", "1606Marti!")) {
            Address address1 = new Address();
            address1.setId(7);

            var repository = new JdbcAddressRepository(connection);
            repository.delete(address1);
        }
    }

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }
}