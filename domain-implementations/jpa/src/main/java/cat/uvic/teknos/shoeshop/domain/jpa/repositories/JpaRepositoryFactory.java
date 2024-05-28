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
    public ClientRepository getClientRepository() throws SQLException {
        return new JpaClientRepository(entityManagerFactory);
    }


    @Override
    public ShoeRepository getShoeRepository() throws SQLException {
        return new JpaShoeRepository(entityManagerFactory);
    }

    @Override
    public ShoeStoreRepository getShoeStoreRepository() throws SQLException {
        return new JpaShoeStoreRepository(entityManagerFactory);
    }


}
