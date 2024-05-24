package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
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

    public JdbcShoeRepository(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public void save(Shoe model) {

    }

    @Override
    public void delete(Shoe model) {

    }

    @Override
    public Shoe get(Integer id) {
        return null;
    }

    @Override
    public Set<Shoe> getAll() {
        return null;
    }
}

