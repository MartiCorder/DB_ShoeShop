package cat.uvic.teknos.shoeshop.domain.jpa.repositories;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.repositories.AddressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

public class JpaAddressRepository implements AddressRepository {
    private final EntityManagerFactory entitymanagerFactory;

    public JpaAddressRepository(EntityManagerFactory entityManagerFactory){
        this.entitymanagerFactory = entityManagerFactory;
    }
    @Override
    public void save(Address model) {
        var entityManager= entitymanagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(model);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Address model) {
        var entityManager = entitymanagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
        entityManager.getTransaction().commit();
    }

    @Override
    public Address get(Integer id) {
        EntityManager entityManager = entitymanagerFactory.createEntityManager();
        Address address = entityManager.find(Address.class, id);
        entityManager.close();
        return address;
    }

    @Override
    public Set<Address> getAll() {
        EntityManager entityManager = entitymanagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Set<Address> addresses = new HashSet<>(entityManager.createQuery("SELECT a FROM Address a", Address.class).getResultList());
        entityManager.getTransaction().commit();
        entityManager.close();
        return addresses;
    }
}
