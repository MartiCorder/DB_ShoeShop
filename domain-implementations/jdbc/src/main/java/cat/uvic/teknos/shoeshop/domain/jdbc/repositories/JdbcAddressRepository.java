package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.repositories.AddressRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcAddressRepository implements AddressRepository {

    private final Connection connection;

    public JdbcAddressRepository(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void save(Address model) {
        String sql = "INSERT INTO ADDRESS (LOCATION) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model.getLocation());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving address: " + e.getMessage(), e);
        }

    }

    @Override
    public void delete(Address model) {
        String sql = "DELETE FROM ADDRESS WHERE ADDRESS_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting address: " + e.getMessage(), e);
        }

    }

    @Override
    public Address get(Integer id) {
        String sql = "SELECT * FROM ADDRESS WHERE ADDRESS_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Address();
                address.setId(resultSet.getInt("ADDRESS_ID"));
                address.setLocation(resultSet.getString("LOCATION"));
                return address;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching address: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<Address> getAll() {
        String sql = "SELECT * FROM ADDRESS";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Set<Address> addresses = new HashSet<>();
            while (resultSet.next()) {
                Address address = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Address();
                address.setId(resultSet.getInt("ADDRESS_ID"));
                address.setLocation(resultSet.getString("LOCATION"));
                addresses.add(address);
            }
            return addresses;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching addresses: " + e.getMessage(), e);
        }
    }
}
