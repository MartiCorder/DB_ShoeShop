package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.repositories.ModelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaModelRepository implements ModelRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaModelRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Model model) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(model);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Model model) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Model get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Model model = entityManager.find(Model.class, id);
        entityManager.close();
        return model;
    }

    @Override
    public Set<Model> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<Model> models = new HashSet<>(entityManager.createQuery("SELECT m FROM Model m", Model.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return models;
    }
}
