package cat.uvic.teknos.shoeshop.domain.jdbc.models;

import java.io.Serializable;

public class ShoeStoreSupplier  implements cat.uvic.teknos.shoeshop.models.ShoeStoreSupplier, Serializable {

   private int shoeStoreId;
    private int supplierId;


    @Override
    public int getShoeStoreId() {
        return shoeStoreId;
    }

    @Override
    public void setShoeStoreId(int shoeStoreId) {

        this.shoeStoreId=shoeStoreId;

    }

    @Override
    public int getSupplierId() {
        return supplierId;
    }

    @Override
    public void setSupplierId(int supplierId) {

        this.supplierId=supplierId;

    }
}
