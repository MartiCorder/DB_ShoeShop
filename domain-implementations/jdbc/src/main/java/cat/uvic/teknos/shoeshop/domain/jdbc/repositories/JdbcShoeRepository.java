package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
public class JdbcShoeRepository implements ShoeRepository {

    private final Connection connection;

    public JdbcShoeRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Shoe shoe) {
        if (shoe.getId() <= 0) {
            insert(shoe);
        } else {
            update(shoe);
        }
    }

    private void insert(Shoe shoe) {
        try {
            connection.setAutoCommit(false);

            Model model = shoe.getModels();
            if (model.getId() <= 0) {
                insertModel(model);
            } else {
                updateModel(model);
            }
            Inventory inventory = shoe.getInventories();
            if (inventory.getId() <= 0) {
                insertInventory(inventory);
            } else {
                updateInventory(inventory);
            }

            String insertShoe = "INSERT INTO SHOE (PRICE, COLOR, SIZE, MODEL_ID, INVENTORY_ID) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertShoe, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setDouble(1, shoe.getPrice());
                stmt.setString(2, shoe.getColor());
                stmt.setString(3, shoe.getSize());
                stmt.setInt(4, model.getId());
                stmt.setInt(5, inventory.getId());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        shoe.setId(keys.getInt(1));
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException(rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void insertModel(Model model) {
        String insertModel = "INSERT INTO MODEL (NAME, BRAND) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertModel, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getBrand());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    model.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateModel(Model model) {
        String updateModel = "UPDATE MODEL SET NAME = ?, BRAND = ? WHERE MODEL_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateModel)) {
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getBrand());
            stmt.setInt(3, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void insertInventory(Inventory inventory) {
        String insertInventory = "INSERT INTO INVENTORY (CAPACITY) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertInventory, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getCapacity());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    inventory.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInventory(Inventory inventory) {
        String updateInventory = "UPDATE INVENTORY SET CAPACITY = ? WHERE INVENTORY_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateInventory)) {
            stmt.setInt(1, inventory.getCapacity());
            stmt.setInt(2, inventory.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Shoe shoe) {
        try {
            connection.setAutoCommit(false);

            String updateShoe = "UPDATE SHOE SET PRICE = ?, COLOR = ?, SIZE = ?, MODEL_ID = ?, INVENTORY_ID = ? WHERE SHOE_ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateShoe)) {
                stmt.setDouble(1, shoe.getPrice());
                stmt.setString(2, shoe.getColor());
                stmt.setString(3, shoe.getSize());
                stmt.setInt(4, shoe.getModels().getId());
                stmt.setInt(5, shoe.getInventories().getId());
                stmt.setInt(6, shoe.getId());
                stmt.executeUpdate();
            }
            updateModel(shoe.getModels());
            updateInventory(shoe.getInventories());

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException(rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Shoe shoe) {
        try {
            connection.setAutoCommit(false);

            String deleteShoe = "DELETE FROM SHOE WHERE SHOE_ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteShoe)) {
                stmt.setInt(1, shoe.getId());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException(rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Shoe get(Integer id) {
        String query = "SELECT * FROM SHOE WHERE SHOE_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Shoe shoe = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe();
                    shoe.setId(rs.getInt("SHOE_ID"));
                    shoe.setPrice(rs.getDouble("PRICE"));
                    shoe.setColor(rs.getString("COLOR"));
                    shoe.setSize(rs.getString("SIZE"));
                    shoe.setModels(findModelById(rs.getInt("MODEL_ID")));
                    shoe.setInventories(findInventoryById(rs.getInt("INVENTORY_ID")));
                    return shoe;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Model findModelById(int modelId) {
        String query = "SELECT * FROM MODEL WHERE MODEL_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, modelId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Model model = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
                    model.setId(rs.getInt("MODEL_ID"));
                    model.setName(rs.getString("NAME"));
                    model.setBrand(rs.getString("BRAND"));
                    return model;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private Inventory findInventoryById(int inventoryId) {
        String query = "SELECT * FROM INVENTORY WHERE INVENTORY_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, inventoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Inventory inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                    inventory.setId(rs.getInt("INVENTORY_ID"));
                    inventory.setCapacity(rs.getInt("CAPACITY"));

                    return inventory;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Shoe> getAll() {
        String query = "SELECT * FROM SHOE";
        Set<Shoe> shoes = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Shoe shoe = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe();
                shoe.setId(rs.getInt("SHOE_ID"));
                shoe.setPrice(rs.getDouble("PRICE"));
                shoe.setColor(rs.getString("COLOR"));
                shoe.setSize(rs.getString("SIZE"));
                shoe.setModels(findModelById(rs.getInt("MODEL_ID")));
                shoe.setInventories(findInventoryById(rs.getInt("INVENTORY_ID")));
                shoes.add(shoe);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoes;
    }
}
