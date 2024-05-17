package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Inventory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
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
    void insertInventoryTest() {

        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Inventory inventory = new Inventory();
            inventory.setCapacity(100);

            entityManager.persist(inventory);

            entityManager.getTransaction().commit();

            assertTrue(inventory.getId() > 0);

            Inventory retrievedInventory = entityManager.find(Inventory.class, inventory.getId());
            assertNotNull(retrievedInventory);
            assertEquals(inventory.getCapacity(), retrievedInventory.getCapacity());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}
