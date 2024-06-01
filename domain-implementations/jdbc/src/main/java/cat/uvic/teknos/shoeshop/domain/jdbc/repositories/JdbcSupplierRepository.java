package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcSupplierRepository implements SupplierRepository {

    private final Connection connection;

    public JdbcSupplierRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Supplier supplier) {
        String sql = "INSERT INTO SUPPLIER (NAME, PHONE) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getPhone());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Supplier supplier) {
        String sql = "DELETE FROM SUPPLIER WHERE SUPPLIER_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplier.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public Supplier get(Integer id) {
        String sql = "SELECT * FROM SUPPLIER WHERE SUPPLIER_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier.setId(resultSet.getInt("SUPPLIER_ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setPhone(resultSet.getString("PHONE"));
                return supplier;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<Supplier> getAll() {
        String sql = "SELECT * FROM SUPPLIER";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Set<Supplier> suppliers = new HashSet<>();
            while (resultSet.next()) {
                Supplier supplier = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
                supplier.setId(resultSet.getInt("SUPPLIER_ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setPhone(resultSet.getString("PHONE"));
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching suppliers: " + e.getMessage(), e);
        }
    }
}
