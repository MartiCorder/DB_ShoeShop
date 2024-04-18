package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.Shoe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ShoeRepositoryTest {
    @Test
    void save() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository(path);
        var shoe = new Shoe();
        shoe.setId(1);
        shoe.setName("Air Max");
        shoe.setModelId(1);
        shoe.setInventoryId(1);
        shoe.setPrice(99.99);
        shoe.setColor("White");
        shoe.setSize("9.5");

        shoeRepository.save(shoe);

        assertTrue(shoe.getId() > 0);
        assertNotNull(shoeRepository.get(shoe.getId()));
        shoeRepository.load();
        assertNotNull(shoeRepository.get(shoe.getId()));
    }
    @Test
    void update() {
        var path = System.getProperty("user.dir") +
                "/src/test/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository(path);
        var shoe = new Shoe();
        shoe.setId(1);
        shoe.setName("Air Max Pro");
        shoe.setModelId(1);
        shoe.setInventoryId(1);
        shoe.setPrice(119.99);
        shoe.setColor("White");
        shoe.setSize("9.5");
        shoeRepository.save(shoe);

        var updatedShoe = shoeRepository.get(1);
        updatedShoe.setName("New Air Max");
        shoeRepository.save(updatedShoe);

        assertEquals(1, updatedShoe.getId());
        assertEquals("New Air Max", updatedShoe.getName());
    }
    @Test
    void delete() {

        var path = System.getProperty("user.dir") + "/src/test/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository(path);
        var shoe = new Shoe();
        shoe.setId(1);
        shoe.setName("Air Max");
        shoe.setModelId(1);
        shoe.setInventoryId(1);
        shoe.setPrice(99.99);
        shoe.setColor("White");
        shoe.setSize("9.5");
        shoeRepository.save(shoe);

        shoeRepository.delete(shoe);

        assertNull(shoeRepository.get(1));
    }
    @Test
    void get() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository(path);
        var shoe = new Shoe();
        shoe.setId(1);
        shoe.setName("Air Max");
        shoe.setModelId(1);
        shoe.setInventoryId(1);
        shoe.setPrice(99.99);
        shoe.setColor("White");
        shoe.setSize("9.5");
        shoeRepository.save(shoe);

        var retrievedShoe = shoeRepository.get(1);

        assertNotNull(retrievedShoe);
        assertEquals(1, retrievedShoe.getId());
        assertEquals("Air Max", retrievedShoe.getName());
    }

    @Test
    void getAll() {
        var path = System.getProperty("user.dir") + "/src/test/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository(path);
        var shoe1 = new Shoe();
        shoe1.setId(1);
        shoe1.setName("Air Max");
        shoe1.setModelId(1);
        shoe1.setInventoryId(1);
        shoe1.setPrice(99.99);
        shoe1.setColor("White");
        shoe1.setSize("9.5");
        shoeRepository.save(shoe1);

        var shoe2 = new Shoe();
        shoe2.setId(2);
        shoe2.setName("Air Force");
        shoe2.setModelId(2);
        shoe2.setInventoryId(2);
        shoe2.setPrice(109.99);
        shoe2.setColor("Black");
        shoe2.setSize("10");
        shoeRepository.save(shoe2);

        var shoes = shoeRepository.getAll();

        assertNotNull(shoes);
        assertEquals(2, shoes.size());
        assertTrue(shoes.contains(shoe1));
        assertTrue(shoes.contains(shoe2));
    }
}