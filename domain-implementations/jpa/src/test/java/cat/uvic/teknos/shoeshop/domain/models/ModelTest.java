package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Model;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
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
    void insertModelTest() {
        // EntityManager
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Model model = new Model();
            model.setName("Air Force");
            model.setBrand("Nike");

            entityManager.persist(model);

            entityManager.getTransaction().commit();

            assertTrue(model.getId() > 0);

            Model retrievedModel = entityManager.find(Model.class, model.getId());
            assertNotNull(retrievedModel);
            assertEquals(model.getName(), retrievedModel.getName());
            assertEquals(model.getBrand(), retrievedModel.getBrand());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}
