package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

public class JdbcShoeStoreRepository implements ShoeStoreRepository {

    private final Connection connection;

    public JdbcShoeStoreRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(ShoeStore model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(ShoeStore model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO SHOESTORE (ID_SHOESTORE) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, model.getName());
            statement.setString(2, model.getOwner());
            statement.setString(3, model.getLocation());
            statement.setInt(4, model.getInventoryId());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting ShoeStore", e);
        }
    }

    private void update(ShoeStore model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE SHOESTORE SET NAME = ?, OWNER = ?, LOCATION = ?, INVENTORY_ID = ? WHERE ID = ?")) {

            statement.setString(1, model.getName());
            statement.setString(2, model.getOwner());
            statement.setString(3, model.getLocation());
            statement.setInt(4, model.getInventoryId());
            statement.setInt(5, model.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating ShoeStore", e);
        }
    }

    @Override
    public void delete(ShoeStore model) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM SHOESTORE WHERE ID = ?")) {
            statement.setInt(1, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item to delete");
            } else {
                System.out.println("ShoeStore deleted successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ShoeStore", e);
        }
    }

    @Override
    public ShoeStore get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOESTORE WHERE ID = ?")) {
            ShoeStore shoeStore = null;

            statement.setInt(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
                shoeStore.setId(resultSet.getInt("ID"));
                shoeStore.setName(resultSet.getString("NAME"));
                shoeStore.setOwner(resultSet.getString("OWNER"));
                shoeStore.setLocation(resultSet.getString("LOCATION"));
                shoeStore.setInventoryId(resultSet.getInt("INVENTORY_ID"));
            }
            return shoeStore;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ShoeStore", e);
        }
    }

    @Override
    public Set<ShoeStore> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOESTORE")) {
            var shoeStores = new HashSet<ShoeStore>();

            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
                shoeStore.setId(resultSet.getInt("ID"));
                shoeStore.setName(resultSet.getString("NAME"));
                shoeStore.setOwner(resultSet.getString("OWNER"));
                shoeStore.setLocation(resultSet.getString("LOCATION"));
                shoeStore.setInventoryId(resultSet.getInt("INVENTORY_ID"));

                shoeStores.add(shoeStore);
            }
            return shoeStores;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ShoeStores", e);
        }
    }
}

