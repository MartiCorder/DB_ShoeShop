package cat.uvic.teknos.shoeshop.domain.jdbc.models;

import java.io.Serializable;

public class ShoeStoreInventory implements cat.uvic.teknos.shoeshop.models.ShoeStoreInventory, Serializable {

    private int shoeStoreId;
    private int inventoryId;
    @Override
    public int getShoeStoreId() {
        return shoeStoreId;
    }

    @Override
    public void setShoeStoreId(int shoeStoreId) {
        this.shoeStoreId=shoeStoreId;
    }

    @Override
    public int getInventoryId() {
        return inventoryId;
    }

    @Override
    public void setInventoryId(int inventoryId) {
        this.inventoryId=inventoryId;
    }
}
