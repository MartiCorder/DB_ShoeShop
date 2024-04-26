package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.repositories.AddressRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

public class JdbcAddressRepository implements AddressRepository {

    private final Connection connection;

    public JdbcAddressRepository(Connection connection){
        this.connection = connection;
    }

    @Override
    public void save(Address model) {
        if (model.getId() >= 0){
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Address model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ADDRESS (ID_ADDRESS) VALUES  (?)", Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, model.getId());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()){
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Address model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE ADDRESS SET ID_ADDRESS=? WHERE ADDRESS_ID=?", Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Address model) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ADDRESS WHERE ID_ADDRESS = ?")) {
            statement.setInt(1, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item to delete");
            } else {
                System.out.println("Correct delete");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Address get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESS WHERE ID = ?")) {
            Address merceders = null;

            statement.setInt(1, id);

            var resultSet= statement.executeQuery();
            if (resultSet.next()) {
                merceders = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Address();
                merceders.setId(resultSet.getInt("ID"));

            }
            return merceders;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Set<Address> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESS")) {
            var cars = new HashSet<Address>();


            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var mercedes = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Address();
                mercedes.setId(resultSet.getInt("ID"));

                cars.add(mercedes);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
