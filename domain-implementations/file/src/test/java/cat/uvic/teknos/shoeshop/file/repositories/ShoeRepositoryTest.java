package cat.uvic.teknos.shoeshop.file.repositories;

import cat.uvic.teknos.shoeshop.file.models.Shoe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoeRepositoryTest {

    @Test
    void save() {

        var path = System.getProperty("user.dir") +
                "/src/main/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository();
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
        ShoeRepository.load();
        assertNotNull(shoeRepository.get(shoe.getId()));
    }

    @Test
    void update() {
        var path = System.getProperty("user.dir") +
                "/src/main/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository();
        var shoe = new Shoe();
        shoe.setId(1);
        shoe.setName("Air Max");
        shoe.setModelId(1);
        shoe.setInventoryId(1);
        shoe.setPrice(99.99);
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

        var path = System.getProperty("user.dir") +
                "/src/main/resources/shoe.ser/";

        var shoeRepository = new ShoeRepository();
        var shoe2 = new Shoe();


        shoeRepository.delete(shoe2);

        assertNull(shoeRepository.get(1));
    }
}
