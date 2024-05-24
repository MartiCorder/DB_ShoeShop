package cat.uvic.teknos.shoeshop.domain.jdbc.repositories;

import cat.uvic.teknos.shoeshop.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private final Connection connection;
    public JdbcRepositoryFactory(){
        try {
            var properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/datasource.properties"));
            connection = DriverManager.getConnection(String.format("%s:%s://%s/%s",
                    properties.getProperty("protocol"),
                    properties.getProperty("subprotocol"),
                    properties.getProperty("url"),
                    properties.getProperty("database")), properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientRepository getClientRepository() throws SQLException {
        return new JdbcClientRepository(connection);
    }

    @Override
    public ShoeRepository getShoeRepository() throws SQLException {
        return new JdbcShoeRepository(connection);
    }

    @Override
    public ShoeStoreRepository getShoeStoreRepository() throws SQLException {
        return new JdbcShoeStoreRepository(connection);
    }

}