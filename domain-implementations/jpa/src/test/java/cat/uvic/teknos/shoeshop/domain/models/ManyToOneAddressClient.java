package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Address;
import cat.uvic.teknos.shoeshop.domain.jpa.models.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManyToOneAddressClient {
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
    void testAddressClientRelation() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();


            Address address = new Address();
            address.setLocation("Example Street 123");


            Client client = new Client("12345678A", "John Doe", "123456789");

            client.
            client.setAddress(address);

            entityManager.persist(address);
            entityManager.persist(client);


            entityManager.getTransaction().commit();

            Client retrievedClient = entityManager.find(Client.class, client.getId());


            assertNotNull(retrievedClient.getAddress());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}

