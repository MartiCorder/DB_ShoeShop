package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcInventoryRepository implements InventoryRepository {

    private final Connection connection;

    public JdbcInventoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Inventory inventory) {
        String sql = "INSERT INTO INVENTORY (CAPACITY) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventory.getCapacity());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Inventory inventory) {
        String sql = "DELETE FROM INVENTORY WHERE INVENTORY_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventory.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public Inventory get(Integer id) {
        String sql = "SELECT * FROM INVENTORY WHERE INVENTORY_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Inventory inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                inventory.setId(resultSet.getInt("INVENTORY_ID"));
                inventory.setCapacity(resultSet.getInt("CAPACITY"));
                return inventory;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<Inventory> getAll() {
        String sql = "SELECT * FROM INVENTORY";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Set<Inventory> inventories = new HashSet<>();
            while (resultSet.next()) {
                Inventory inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                inventory.setId(resultSet.getInt("INVENTORY_ID"));
                inventory.setCapacity(resultSet.getInt("CAPACITY"));
                inventories.add(inventory);
            }
            return inventories;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching inventories: " + e.getMessage(), e);
        }
    }
}
