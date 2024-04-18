package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.ShoeStore;
import org.junit.jupiter.api.Test;

import javax.naming.Name;
import java.util.jar.Attributes;
import static org.junit.jupiter.api.Assertions.*;
class ShoeStoreRepositoryTest {
    @Test
    void save() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoestore.ser/";
        var repository = new ShoeStoreRepository(path);

        var shopTaradell = new ShoeStore();
        shopTaradell.setId(1);
        shopTaradell.setName("Corder Shop");
        shopTaradell.setInventoryId(1);
        shopTaradell.setLocation("Taradell");
        shopTaradell.setOwner("Martí Corder");

        repository.save(shopTaradell);

        assertTrue(shopTaradell.getId() > 0);
        assertNotNull(repository.get(shopTaradell.getId()));
        repository.load();
        assertNotNull(repository.get(shopTaradell.getId()));
    }

    @Test
    void update() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoestore.ser/";
        var repository = new ShoeStoreRepository(path);

        var shopTaradell = new ShoeStore();
        shopTaradell.setId(1);
        shopTaradell.setName("Corder's Shop");
        repository.save(shopTaradell);

        assertNotNull(repository.get(1));
        var updatedShoeStore = repository.get(1);
        assertEquals(1, updatedShoeStore.getId());
        assertEquals("Corder's Shop", updatedShoeStore.getName());
    }

    @Test
    void delete() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoestore.ser/";
        var repository = new ShoeStoreRepository(path);

        var shop = new ShoeStore();
        shop.setId(1);
        shop.setName("Test Shop");
        shop.setLocation("Test Location");

        repository.save(shop);
        assertNotNull(repository.get(1));

        repository.delete(shop);
        assertNull(repository.get(1));
    }

    @Test
    void getAll() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoestore.ser/";
        var repository = new ShoeStoreRepository(path);

        var shop1 = new ShoeStore();
        shop1.setId(1);
        shop1.setName("Shop 1");
        shop1.setLocation("Location 1");

        var shop2 = new ShoeStore();
        shop2.setId(2);
        shop2.setName("Shop 2");
        shop2.setLocation("Location 2");

        repository.save(shop1);
        repository.save(shop2);

        var allShops = repository.getAll();
        assertEquals(2, allShops.size());
        assertTrue(allShops.contains(shop1));
        assertTrue(allShops.contains(shop2));
    }
}