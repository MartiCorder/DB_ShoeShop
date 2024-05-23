package cat.uvic.teknos.shoeshop.domain.jdbc.models;

import java.io.Serializable;

public class ShoeStoreClient implements cat.uvic.teknos.shoeshop.models.ShoeStoreClient, Serializable {

    private int shoeStoreId;
    private int clientId;

    @Override
    public int getShoeStoreId() {
        return shoeStoreId;
    }

    @Override
    public void setShoeStoreId(int shoeStoreId) {
        this.shoeStoreId=shoeStoreId;
    }

    @Override
    public int getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(int clientId) {
        this.clientId=clientId;
    }
}
