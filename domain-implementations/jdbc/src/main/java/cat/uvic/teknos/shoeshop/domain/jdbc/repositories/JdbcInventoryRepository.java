package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;
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

public class JdbcInventoryRepository implements InventoryRepository{


    private final Connection connection;

    public JdbcInventoryRepository(Connection connection){
        this.connection = connection;
    }

    @Override
    public void save(Inventory model) {
        if (model.getId() >= 0){
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Inventory model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO INVENTORY (ID_INVENTORY) VALUES  (?, ?)", Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, model.getId());
            statement.setInt(2, model.getCapacity());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()){
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Inventory model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE INVENTORY SET ID_INVENTORY=? SET CAPACITY = ? WHERE INVENTORY_ID=?", Statement.RETURN_GENERATED_KEYS)){

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
    public void delete(Inventory model) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM INVENTORY WHERE ID_INVENTORY = ?")) {
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
    public Inventory get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM INVENTORY WHERE ID = ?")) {
            Inventory inventory = null;

            statement.setInt(1, id);

            var resultSet= statement.executeQuery();
            if (resultSet.next()) {
                inventory = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                inventory.setId(resultSet.getInt("ID"));

            }
            return inventory;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Set<Inventory> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM INVENTORY")) {
            var inventory = new HashSet<Inventory>();


            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var inventory1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
                inventory1.setId(resultSet.getInt("ID"));

                inventory.add(inventory1);
            }
            return inventory;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

}
