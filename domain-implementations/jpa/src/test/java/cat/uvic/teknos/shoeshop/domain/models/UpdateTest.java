package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UpdateTest {
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
    void updateAddressTest() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();


            Address address = entityManager.find(Address.class, 1); //El segon es una id que existeixi

            assertNotNull(address);


            String newLocation = "Taradell";
            address.setLocation(newLocation);


            entityManager.getTransaction().commit();

            Address updatedAddress = entityManager.find(Address.class, 1);//Aqui el mateix
            assertNotNull(updatedAddress);
            assertEquals(newLocation, updatedAddress.getLocation());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }



}
