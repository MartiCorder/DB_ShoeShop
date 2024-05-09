package cat.uvic.teknos.shoeshop.domain.jdbc.models;

import cat.uvic.teknos.shoeshop.models.*;
import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.Client;
public class JdbcModelFactory implements ModelFactory{
    @Override
    public Address createAddress() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Address();
    }

    @Override
    public Supplier createSupplier() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier();
    }

    @Override
    public Model createModel() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Model();
    }
    @Override
    public Shoe createShoe() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Shoe();
    }
    @Override
    public ShoeStore createShoeStore() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.ShoeStore();
    }
    @Override
    public Inventory createInventory(){
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Inventory();
    }
    @Override
    public Client createClient() {
        return new cat.uvic.teknos.shoeshop.domain.jdbc.models.Client();
    }
}