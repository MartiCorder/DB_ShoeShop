package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.repositories.InventoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaInventoryRepository implements InventoryRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaInventoryRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Inventory model) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(model);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Inventory model) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Inventory get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Inventory inventory = entityManager.find(Inventory.class, id);
        entityManager.close();
        return inventory;
    }

    @Override
    public Set<Inventory> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<Inventory> inventories = new HashSet<>(entityManager.createQuery("SELECT i FROM Inventory i", Inventory.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return inventories;
    }
}

