package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaSupplierRepository implements SupplierRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaSupplierRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Supplier supplier) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(supplier);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Supplier supplier) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(supplier) ? supplier : entityManager.merge(supplier));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Supplier get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Supplier supplier = entityManager.find(Supplier.class, id);
        entityManager.close();
        return supplier;
    }

    @Override
    public Set<Supplier> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<Supplier> suppliers = new HashSet<>(entityManager.createQuery("SELECT s FROM Supplier s", Supplier.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return suppliers;
    }
}

