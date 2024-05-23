package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.ShoeStore;
import cat.uvic.teknos.shoeshop.domain.jpa.models.Supplier;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ShoeStoreTest {
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
    void insertShoeStoreTest() {

        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Supplier supplier = new Supplier();
            supplier.setName("ShoeManager");
            entityManager.persist(supplier);

            ShoeStore shoeStore = new ShoeStore();
            shoeStore.setName("Far");
            shoeStore.setOwner("Marti Corder");
            shoeStore.setLocation("Vic");

            entityManager.persist(shoeStore);

            entityManager.getTransaction().commit();

            assertTrue(shoeStore.getId() > 0);

            ShoeStore retrievedShoeStore = entityManager.find(ShoeStore.class, shoeStore.getId());
            assertNotNull(retrievedShoeStore);
            assertEquals(shoeStore.getName(), retrievedShoeStore.getName());
            assertEquals(shoeStore.getOwner(), retrievedShoeStore.getOwner());
            assertEquals(shoeStore.getLocation(), retrievedShoeStore.getLocation());

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}
