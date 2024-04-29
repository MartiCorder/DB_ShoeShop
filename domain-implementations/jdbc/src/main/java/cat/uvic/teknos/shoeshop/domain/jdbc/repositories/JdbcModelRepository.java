package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

public class JdbcModelRepository implements ModelRepository {

    private final Connection connection;

    public JdbcModelRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Model model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Model model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO MODEL (ID_MODEL, NAME, BRAND) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, model.getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getBrand());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting Model", e);
        }
    }

    private void update(Model model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE MODEL SET NAME = ?, BRAND = ? WHERE ID_MODEL = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getName());
            statement.setString(2, model.getBrand());
            statement.setInt(3, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Model", e);
        }
    }

    @Override
    public void delete(Model model) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM MODEL WHERE ID_MODEL = ?")) {
            statement.setInt(1, model.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item to delete");
            } else {
                System.out.println("Correct delete");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Model", e);
        }
    }

    @Override
    public Model get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM MODEL WHERE ID_MODEL = ?")) {
            Model model = null;

            statement.setInt(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                model = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
                model.setId(resultSet.getInt("ID_MODEL"));
                model.setName(resultSet.getString("NAME"));
                model.setBrand(resultSet.getString("BRAND"));
            }
            return model;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Model", e);
        }
    }

    @Override
    public Set<Model> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM MODEL")) {
            var models = new HashSet<Model>();

            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var model = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
                model.setId(resultSet.getInt("ID_MODEL"));
                model.setName(resultSet.getString("NAME"));
                model.setBrand(resultSet.getString("BRAND"));

                models.add(model);
            }
            return models;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all Models", e);
        }
    }
}

