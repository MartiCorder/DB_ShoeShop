package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.repositories.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.Properties;

public class JpaRepositoryFactory implements RepositoryFactory {
    private final EntityManagerFactory entityManagerFactory;

    public JpaRepositoryFactory() {
        try {
            var properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/jpa.properties"));
            this.entityManagerFactory = Persistence.createEntityManagerFactory("shoeshopjpa", properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientRepository getClientRepository() {
        return new JpaClientRepository(entityManagerFactory);
    }

    @Override
    public ShoeRepository getShoeRepository() {
        return new JpaShoeRepository(entityManagerFactory);
    }

    @Override
    public ShoeStoreRepository getShoeStoreRepository() {
        return new JpaShoeStoreRepository(entityManagerFactory);
    }

    @Override
    public InventoryRepository getInventoryRepository() {
        return new JpaInventoryRepository(entityManagerFactory);
    }

    @Override
    public AddressRepository getAddressRepository() {
        return new JpaAddressRepository(entityManagerFactory);
    }

    @Override
    public SupplierRepository getSupplierRepository() {
        return new JpaSupplierRepository(entityManagerFactory);
    }

    @Override
    public ModelRepository getModelRepository() {
        return new JpaModelRepository(entityManagerFactory);
    }
}
