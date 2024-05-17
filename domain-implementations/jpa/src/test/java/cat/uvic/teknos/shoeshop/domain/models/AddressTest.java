package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressTest {
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("shoeshopjpa");
    }

    @AfterAll
    static void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void insertAddressTest() {
        // EntityManager
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Address address = new Address();
            address.setLocation("Example Street 123");

            entityManager.persist(address);

            entityManager.getTransaction().commit();

            // Verificar que se ha asignado un ID
            assertTrue(address.getId() > 0);

            // Verificar que la dirección se ha guardado correctamente
            Address retrievedAddress = entityManager.find(Address.class, address.getId());
            assertNotNull(retrievedAddress);
            assertTrue(retrievedAddress.getLocation().equals(address.getLocation()));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}

