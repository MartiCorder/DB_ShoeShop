package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Supplier;
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
        try {
            connection.setAutoCommit(false);

            String insertShoeStore = "INSERT INTO SHOE_STORE (NAME, OWNER, LOCATION) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertShoeStore, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, model.getName());
                stmt.setString(2, model.getOwner());
                stmt.setString(3, model.getLocation());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        model.setId(keys.getInt(1));
                    }
                }
            }

            if (model.getInventories() != null) {
                for (Inventory inventory : model.getInventories()) {
                    saveShoeStoreInventories(inventory, model.getId());
                }
            }

            if (model.getSuppliers() != null) {
                for (Supplier supplier : model.getSuppliers()) {
                    saveShoeStoreSuppliers(supplier, model.getId());
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

    private void saveShoeStoreInventories(Inventory inventory, int storeId) {
        String insertInventory = "INSERT INTO INVENTORY (CAPACITY) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertInventory, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getCapacity());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    inventory.setId(keys.getInt(1));
                }
            }

            String insertShoeStoreInventory = "INSERT INTO SHOE_STORE_INVENTORY (SHOE_STORE_ID, INVENTORY_ID) VALUES (?, ?)";
            try (PreparedStatement stmt2 = connection.prepareStatement(insertShoeStoreInventory)) {
                stmt2.setInt(1, storeId);
                stmt2.setInt(2, inventory.getId());
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveShoeStoreSuppliers(Supplier supplier, int storeId) {
        String insertSupplier = "INSERT INTO SUPPLIER (NAME, PHONE) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSupplier, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    supplier.setId(keys.getInt(1));
                }
            }

            String insertShoeStoreSupplier = "INSERT INTO SHOE_STORE_SUPPLIER (SHOE_STORE_ID, SUPPLIER_ID) VALUES (?, ?)";
            try (PreparedStatement stmt2 = connection.prepareStatement(insertShoeStoreSupplier)) {
                stmt2.setInt(1, storeId);
                stmt2.setInt(2, supplier.getId());
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(ShoeStore model) {
        try {
            connection.setAutoCommit(false);

            String updateShoeStore = "UPDATE SHOE_STORE SET NAME = ?, OWNER = ?, LOCATION = ? WHERE SHOE_STORE_ID = ?";
            try (PreparedStatement shoeStoreStatement = connection.prepareStatement(updateShoeStore)) {
                shoeStoreStatement.setString(1, model.getName());
                shoeStoreStatement.setString(2, model.getOwner());
                shoeStoreStatement.setString(3, model.getLocation());
                shoeStoreStatement.setInt(4, model.getId());

                shoeStoreStatement.executeUpdate();
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
    public void delete(ShoeStore shoeStore) {
        try {
            connection.setAutoCommit(false);

            String deleteShoeStore = "DELETE FROM SHOE_STORE WHERE SHOE_STORE_ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteShoeStore)) {
                stmt.setInt(1, shoeStore.getId());
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
    public ShoeStore get(Integer id) {
        String query = "SELECT * FROM SHOE_STORE WHERE SHOE_STORE_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
                    shoeStore.setId(rs.getInt("SHOE_STORE_ID"));
                    shoeStore.setName(rs.getString("NAME"));
                    shoeStore.setOwner(rs.getString("OWNER"));
                    shoeStore.setLocation(rs.getString("LOCATION"));
                    shoeStore.setInventories(findInventoriesByStoreId(id));
                    shoeStore.setSuppliers(findSuppliersByStoreId(id));
                    return shoeStore;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Set<Inventory> findInventoriesByStoreId(int storeId) {
        String query = "SELECT i.* FROM INVENTORY i JOIN SHOE_STORE_INVENTORY si ON i.INVENTORY_ID = si.INVENTORY_ID WHERE si.SHOE_STORE_ID = ?";
        Set<Inventory> inventories = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inventory inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                    inventory.setId(rs.getInt("INVENTORY_ID"));
                    inventory.setCapacity(rs.getInt("CAPACITY"));
                    inventories.add(inventory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventories;
    }

    private Set<Supplier> findSuppliersByStoreId(int storeId) {
        String query = "SELECT s.* FROM SUPPLIER s JOIN SHOE_STORE_SUPPLIER ss ON s.SUPPLIER_ID = ss.SUPPLIER_ID WHERE ss.SHOE_STORE_ID = ?";
        Set<Supplier> suppliers = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                    supplier.setId(rs.getInt("SUPPLIER_ID"));
                    supplier.setName(rs.getString("NAME"));
                    supplier.setPhone(rs.getString("PHONE"));
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return suppliers;
    }

    @Override
    public Set<ShoeStore> getAll() {
        String query = "SELECT * FROM SHOE_STORE";
        Set<ShoeStore> shoeStores = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
                shoeStore.setId(rs.getInt("SHOE_STORE_ID"));
                shoeStore.setName(rs.getString("NAME"));
                shoeStore.setOwner(rs.getString("OWNER"));
                shoeStore.setLocation(rs.getString("LOCATION"));
                shoeStore.setInventories(findInventoriesByStoreId(shoeStore.getId()));
                shoeStore.setSuppliers(findSuppliersByStoreId(shoeStore.getId()));
                shoeStores.add(shoeStore);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoeStores;
    }
}
