package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.Address;
import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.repositories.ClientRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})

public class JdbcClientRepository implements ClientRepository {

    private final Connection connection;

    public JdbcClientRepository(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public void save(Client model) {

    }

    @Override
    public void delete(Client model) {

    }

    @Override
    public Client get(Integer id) {
        return null;
    }

    @Override
    public Set<Client> getAll() {
        return null;
    }
}
