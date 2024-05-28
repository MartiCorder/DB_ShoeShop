package cat.uvic.teknos.shoeshop.domain.jpa.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe;
import cat.uvic.teknos.shoeshop.domain.jdbc.models.Model;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcShoeRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcShoeRepositoryTest {

    private final Connection connection;

    public JdbcShoeRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    public void shouldInsertShoe() throws SQLException {
        Model model = new Model();
        model.setName("Test Model");
        model.setBrand("Test Brand");

        Shoe shoe = new Shoe();
        shoe.setPrice(100.0);
        shoe.setColor("Red");
        shoe.setSize("42");
        shoe.setModels(model);

        var repository = new JdbcShoeRepository(connection);
        repository.save(shoe);

        Shoe fetchedShoe = (Shoe) repository.get(shoe.getId());
        assertNotNull(fetchedShoe);
        assertEquals(shoe.getPrice(), fetchedShoe.getPrice());
        assertEquals(shoe.getColor(), fetchedShoe.getColor());
        assertEquals(shoe.getSize(), fetchedShoe.getSize());
        assertNotNull(fetchedShoe.getModels());
        assertEquals(model.getName(), fetchedShoe.getModels().getName());
        assertEquals(model.getBrand(), fetchedShoe.getModels().getBrand());
    }

    @Test
    public void shouldUpdateShoe() throws SQLException {
        Model model = new Model();
        model.setId(2);
        model.setName("Test Model");
        model.setBrand("Test Brand");

        Shoe shoe = new Shoe();
        shoe.setId(2);
        shoe.setPrice(100.0);
        shoe.setColor("Red");
        shoe.setSize("42");
        shoe.setModels(model);

        var repository = new JdbcShoeRepository(connection);

        repository.save(shoe);

        Shoe fetchedShoe = (Shoe) repository.get(shoe.getId());
        assertNotNull(fetchedShoe);
        assertEquals(shoe.getPrice(), fetchedShoe.getPrice());
        assertEquals(shoe.getColor(), fetchedShoe.getColor());
        assertEquals(shoe.getSize(), fetchedShoe.getSize());
        assertNotNull(fetchedShoe.getModels());
        assertEquals(shoe.getModels().getName(), fetchedShoe.getModels().getName());
        assertEquals(shoe.getModels().getBrand(), fetchedShoe.getModels().getBrand());
    }

    @Test
    public void shouldDeleteShoe() throws SQLException {
        Model model = new Model();
        model.setId(2);

        Shoe shoe = new Shoe();
        shoe.setId(2);
        shoe.setModels(model);

        var repository = new JdbcShoeRepository(connection);
        repository.save(shoe);

        repository.delete(shoe);

        Shoe fetchedShoe = (Shoe) repository.get(shoe.getId());
        assertNull(fetchedShoe);
    }

    @Test
    public void shouldGetAllShoes() throws SQLException {

        var repository = new JdbcShoeRepository(connection);

        Set<cat.uvic.teknos.shoeshop.models.Shoe> shoes = repository.getAll();

        assertNotNull(shoes);
    }
}
