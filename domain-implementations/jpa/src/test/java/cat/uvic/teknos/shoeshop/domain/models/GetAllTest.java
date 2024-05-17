package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetAllTest {
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("shoeshopjpa");
    }

    @AfterAll
    static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    @Test
    void getAllAddressTest() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();


            List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a", Address.class)
                    .getResultList();


            entityManager.getTransaction().commit();


            assertNotNull(addresses);
            assertEquals(1, addresses.size());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}

