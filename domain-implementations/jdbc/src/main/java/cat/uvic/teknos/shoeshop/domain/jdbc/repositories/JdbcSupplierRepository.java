package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;
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

public class JdbcSupplierRepository implements SupplierRepository {

    private final Connection connection;

    public JdbcSupplierRepository(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public void save(Supplier model) {
        if (model.getId() <= 0){
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO SUPPLIER (ID_SUPPLIER, NAME, PHONE) VALUES  (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, model.getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getPhone());
            statement.executeUpdate();
            connection.commit();

            var keys = statement.getGeneratedKeys();
            if (keys.next()){
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE SUPPLIER SET NAME = ?, PHONE=? WHERE ID_SUPPLIER=?",
                Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, model.getName());
            statement.setString(2, model.getPhone());
            statement.setInt(3, model.getId());

            int rowsAffected = statement.executeUpdate();
            connection.commit();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM SUPPLIER WHERE ID_SUPPLIER = ?")) {

            statement.setInt(1, model.getId());

            int rowsAffected = statement.executeUpdate();
            connection.commit();
            if (rowsAffected == 0) {
                System.out.println("No item to delete");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Supplier get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM SUPPLIER WHERE ID_SUPPLIER = ?")) {
            Supplier supplier1 = null;

            statement.setInt(1, id);

            var resultSet= statement.executeQuery();
            if (resultSet.next()) {
                supplier1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier1.setId(resultSet.getInt("ID_SUPPLIER"));

            }
            return supplier1;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Set<Supplier> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUPPLIER")) {
            var suppliers = new HashSet<Supplier>();


            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var supplier1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier1.setId(resultSet.getInt("ID_SUPPLIER"));
                supplier1.setName(resultSet.getString("NAME"));
                supplier1.setPhone(resultSet.getString("PHONE"));
                suppliers.add(supplier1);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
