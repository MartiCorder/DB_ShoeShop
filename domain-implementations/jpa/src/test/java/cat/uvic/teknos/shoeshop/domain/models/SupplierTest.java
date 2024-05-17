package cat.uvic.teknos.shoeshop.domain.models;

import cat.uvic.teknos.shoeshop.domain.jpa.models.Supplier;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {
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
    void insertSupplierTest() {
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Supplier supplier = new Supplier();
            supplier.setName("Example Supplier");
            supplier.setPhone("123456789");

            entityManager.persist(supplier);

            entityManager.getTransaction().commit();

            assertTrue(supplier.getId() > 0);

            Supplier retrievedSupplier = entityManager.find(Supplier.class, supplier.getId());
            assertNotNull(retrievedSupplier);
            assertEquals(supplier.getName(), retrievedSupplier.getName());
            assertEquals(supplier.getPhone(), retrievedSupplier.getPhone());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}

