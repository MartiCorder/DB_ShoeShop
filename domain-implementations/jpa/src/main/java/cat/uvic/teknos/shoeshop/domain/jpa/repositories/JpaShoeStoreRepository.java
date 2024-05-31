package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.ShoeStoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaShoeStoreRepository implements ShoeStoreRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaShoeStoreRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(ShoeStore shoeStore) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(shoeStore);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(ShoeStore shoeStore) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(shoeStore) ? shoeStore : entityManager.merge(shoeStore));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public ShoeStore get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ShoeStore shoeStore = entityManager.find(ShoeStore.class, id);
        entityManager.close();
        return shoeStore;
    }

    @Override
    public Set<ShoeStore> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<ShoeStore> shoeStores = new HashSet<>(entityManager.createQuery("SELECT ss FROM ShoeStore ss", ShoeStore.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return shoeStores;
    }
}
