package cat.uvic.teknos.shoeshop.repositories;

import java.sql.SQLException;

public interface RepositoryFactory {

    ClientRepository getClientRepository() throws SQLException;

    ShoeRepository getShoeRepository() throws SQLException;
    ShoeStoreRepository getShoeStoreRepository() throws SQLException;

}
