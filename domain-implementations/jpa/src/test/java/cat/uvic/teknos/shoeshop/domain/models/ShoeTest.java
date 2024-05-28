/*package cat.uvic.teknos.shoeshop.domain.models;


import cat.uvic.teknos.shoeshop.domain.jpa.models.Inventory;
import cat.uvic.teknos.shoeshop.domain.jpa.models.Model;
import cat.uvic.teknos.shoeshop.domain.jpa.models.Shoe;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ShoeTest {
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
    void insertShoeTest() {
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Model model = new Model();
            model.setName("Dunk");
            model.setBrand("NIke");
            entityManager.persist(model);

            Inventory inventory = new Inventory();
            inventory.setCapacity(100);
            entityManager.persist(inventory);

            Shoe shoe = new Shoe(model, inventory, 99.99, "White", "43");
            entityManager.persist(shoe);

            entityManager.getTransaction().commit();

            assertTrue(shoe.getId() > 0);

            Shoe retrievedShoe = entityManager.find(Shoe.class, shoe.getId());
            assertNotNull(retrievedShoe);
            assertEquals(shoe.getPrice(), retrievedShoe.getPrice());
            assertEquals(shoe.getColor(), retrievedShoe.getColor());
            assertEquals(shoe.getSize(), retrievedShoe.getSize());
            assertEquals(shoe.getModelId(), retrievedShoe.getModelId());
            assertEquals(shoe.getInventoryId(), retrievedShoe.getInventoryId());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}*/
