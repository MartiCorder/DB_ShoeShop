package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
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

public class JdbcClientRepository implements ClientRepository{

    private final Connection connection;

    public JdbcClientRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Client model) {

        if (model.getId() <= 0){
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Client model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CLIENT (ID_CLIENT, DNI,  NAME, PHONE) VALUES  (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, model.getId());
            statement.setString(2, model.getDni());
            statement.setString(3, model.getName());
            statement.setString(4, model.getPhone());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()){
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Client model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE CLIENT SET DNI=?, NAME=?, PHONE=? WHERE ID_CLIENT=?", Statement.RETURN_GENERATED_KEYS)){


            statement.setString(1, model.getDni());
            statement.setString(2, model.getName());
            statement.setString(3, model.getPhone());
            statement.setInt(4, model.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No items to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Client model) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM CLIENT WHERE ID_CLIENT = ?")) {
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
    public Client get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CLIENT WHERE ID_CLIENT = ?")) {
            Client client1 = null;

            statement.setInt(1, id);

            var resultSet= statement.executeQuery();
            if (resultSet.next()) {
                client1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Client();
                client1.setId(resultSet.getInt("ID_CLIENT"));

            }
            return client1;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public Set<Client> getAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CLIENT")) {
            var clients = new HashSet<Client>();


            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var client1 = new cat.uvic.teknos.shoeshop.domain.jdbc.models.Client();
                client1.setId(resultSet.getInt("ID_CLIENT"));
                client1.setDni(resultSet.getString("DNI"));
                client1.setName(resultSet.getString("NAME"));
                client1.setPhone(resultSet.getString("PHONE"));

                clients.add(client1);
            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

}
