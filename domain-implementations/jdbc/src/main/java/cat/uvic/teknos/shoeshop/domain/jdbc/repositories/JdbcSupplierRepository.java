package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

public class JdbcSupplierRepository implements SupplierRepository {

    private final Connection connection;

    public JdbcSupplierRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Supplier model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO SUPPLIER (ID_SUPPLIER) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, model.getName());
            statement.setString(2, model.getPhone());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting Supplier", e);
        }
    }

    private void update(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE SUPPLIER SET NAME = ?, PHONE = ? WHERE ID = ?")) {

            statement.setString(1, model.getName());
            statement.setString(2, model.getPhone());
            statement.setInt(3, model.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Supplier", e);
        }
    }

    @Override
    public void delete(Supplier model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM SUPPLIER WHERE ID = ?")) {
            statement.setInt(1, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item to delete");
            } else {
                System.out.println("Supplier deleted successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Supplier", e);
        }
    }

    @Override
    public Supplier get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM SUPPLIER WHERE ID = ?")) {

            Supplier supplier = null;
            statement.setInt(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier.setId(resultSet.getInt("ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setPhone(resultSet.getString("PHONE"));
            }
            return supplier;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Supplier", e);
        }
    }

    @Override
    public Set<Supplier> getAll() {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM SUPPLIER")) {

            var suppliers = new HashSet<Supplier>();

            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier.setId(resultSet.getInt("ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setPhone(resultSet.getString("PHONE"));

                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Suppliers", e);
        }
    }
}
