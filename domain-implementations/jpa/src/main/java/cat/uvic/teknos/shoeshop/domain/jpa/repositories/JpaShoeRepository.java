package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.repositories.ShoeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaShoeRepository implements ShoeRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaShoeRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Shoe shoe) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(shoe);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Shoe shoe) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(shoe) ? shoe : entityManager.merge(shoe));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Shoe get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Shoe shoe = entityManager.find(Shoe.class, id);
        entityManager.close();
        return shoe;
    }

    @Override
    public Set<Shoe> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<Shoe> shoes = new HashSet<>(entityManager.createQuery("SELECT s FROM Shoe s", Shoe.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return shoes;
    }
}

