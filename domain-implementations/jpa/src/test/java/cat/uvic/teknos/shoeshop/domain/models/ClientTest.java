package cat.uvic.teknos.shoeshop.domain.models;


import cat.uvic.teknos.shoeshop.domain.jpa.models.Client;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
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
    void insertClientTest() {

        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Client client = new Client();
            client.setDni("14525678F");
            client.setName("Marti");
            client.setPhone("744485606");

            entityManager.persist(client);

            entityManager.getTransaction().commit();

            assertTrue(client.getId() > 0);

            Client retrievedClient = entityManager.find(Client.class, client.getId());
            assertNotNull(retrievedClient);
            assertEquals(client.getDni(), retrievedClient.getDni());
            assertEquals(client.getName(), retrievedClient.getName());
            assertEquals(client.getPhone(), retrievedClient.getPhone());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}

