package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.repositories.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.SQLException;

public class JpaRepositoryFactory implements RepositoryFactory {
    private final EntityManagerFactory entityManagerFactory;

    public JpaRepositoryFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("shoeshopjpa");
    }
    @Override
    public AddressRepository getAddressRepository() throws SQLException {
        return new JpaAddressRepository(entityManagerFactory);
    }

    @Override
    public ClientRepository getClientRepository() throws SQLException {
        return new JpaClientRepository(entityManagerFactory);
    }

    @Override
    public InventoryRepository getInventoryRepository() throws SQLException {
        return new JpaInventoryRepository(entityManagerFactory);
    }

    @Override
    public ModelRepository getModelRepository() throws SQLException {
        return new JpaModelRepository(entityManagerFactory);
    }

    @Override
    public ShoeRepository getShoeRepository() throws SQLException {
        return new JpaShoeRepository(entityManagerFactory);
    }

    @Override
    public ShoeStoreRepository getShoeStoreRepository() throws SQLException {
        return new JpaShoeStoreRepository(entityManagerFactory);
    }

    @Override
    public SupplierRepository getSupplierRepository() throws SQLException {
        return new JpaSupplierRepository(entityManagerFactory);
    }
}
