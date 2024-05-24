package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
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

    public JdbcShoeStoreRepository(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public void save(ShoeStore model) {

    }

    @Override
    public void delete(ShoeStore model) {

    }

    @Override
    public ShoeStore get(Integer id) {
        return null;
    }

    @Override
    public Set<ShoeStore> getAll() {
        return null;
    }
}

