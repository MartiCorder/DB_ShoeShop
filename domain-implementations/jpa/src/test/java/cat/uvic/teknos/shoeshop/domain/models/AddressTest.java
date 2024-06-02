package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    private static EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("shoeshopjpa");
    }

    @AfterEach
    void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void insertAddressTest() {

        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Address address = new Address();
            address.setLocation("Carrer Roure, 23");

            entityManager.persist(address);

            entityManager.getTransaction().commit();

            assertTrue(address.getId() > 0);

            Address retrievedAddress = entityManager.find(Address.class, address.getId());
            assertNotNull(retrievedAddress);
            assertEquals(retrievedAddress.getLocation(), address.getLocation());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}

