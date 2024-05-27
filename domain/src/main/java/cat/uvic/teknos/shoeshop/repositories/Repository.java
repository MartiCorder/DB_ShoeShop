package cat.uvic.teknos.shoeshop.repositories;

import java.sql.SQLException;
import java.util.Set;

public interface Repository<K,V> {
    void save(V model) throws SQLException;

    void delete(V model) throws SQLException;
    V get(K id) throws SQLException;
    Set<V> getAll() throws SQLException;

}
