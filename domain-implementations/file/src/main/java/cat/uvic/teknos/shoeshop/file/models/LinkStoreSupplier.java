package cat.uvic.teknos.shoeshop.file.models;
import java.io.Serializable;

public class LinkStoreSupplier implements cat.uvic.teknos.shoeshop.models.LinkStoreSupplier, Serializable{
    private int id;
    private int id2;
    @Override
    public int getStoreId() {
        return id;
    }

    @Override
    public void setStoreId(int id) {
        this.id=id;
    }

    @Override
    public int getSupplierId() {
        return id2;
    }

    @Override
    public void setSupplierId(int id_supplier) {

        this.id2=id_supplier;

    }
}
