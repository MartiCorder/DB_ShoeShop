package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteTest {
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
    void deleteAddressTest() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Address address = entityManager.find(Address.class, 1);//Id de l'eliminat


            entityManager.remove(address);

            entityManager.getTransaction().commit();


            Address deletedAddress = entityManager.find(Address.class, 1);//El mateix
            assertNull(deletedAddress);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            fail("Failed to delete address: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }


}

