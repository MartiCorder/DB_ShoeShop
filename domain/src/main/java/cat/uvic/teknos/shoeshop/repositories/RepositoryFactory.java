package cat.uvic.teknos.shoeshop.repositories;

import java.sql.SQLException;

public interface RepositoryFactory {

    AddressRepository getAddressRepository() throws SQLException;
    ClientRepository getClientRepository() throws SQLException;
    InventoryRepository getInventoryRepository() throws SQLException;
    ModelRepository getModelRepository() throws SQLException;
    ShoeRepository getShoeRepository() throws SQLException;
    ShoeStoreRepository getShoeStoreRepository() throws SQLException;
    SupplierRepository getSupplierRepository() throws SQLException;
}
