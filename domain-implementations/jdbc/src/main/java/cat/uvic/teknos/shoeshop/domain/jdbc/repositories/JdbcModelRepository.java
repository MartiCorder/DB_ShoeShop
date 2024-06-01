package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcModelRepository implements ModelRepository {

    private final Connection connection;

    public JdbcModelRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Model model) {
        String sql = "INSERT INTO MODEL (NAME, BRAND) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model.getName());
            statement.setString(2, model.getBrand());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving model: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Model model) {
        String sql = "DELETE FROM MODEL WHERE MODEL_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting model: " + e.getMessage(), e);
        }
    }

    @Override
    public Model get(Integer id) {
        String sql = "SELECT * FROM MODEL WHERE MODEL_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Model model = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
                model.setId(resultSet.getInt("MODEL_ID"));
                model.setName(resultSet.getString("NAME"));
                model.setBrand(resultSet.getString("BRAND"));
                return model;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching model: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<Model> getAll() {
        String sql = "SELECT * FROM MODEL";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Set<Model> models = new HashSet<>();
            while (resultSet.next()) {
                Model model = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
                model.setId(resultSet.getInt("MODEL_ID"));
                model.setName(resultSet.getString("NAME"));
                model.setBrand(resultSet.getString("BRAND"));
                models.add(model);
            }
            return models;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching models: " + e.getMessage(), e);
        }
    }
}
