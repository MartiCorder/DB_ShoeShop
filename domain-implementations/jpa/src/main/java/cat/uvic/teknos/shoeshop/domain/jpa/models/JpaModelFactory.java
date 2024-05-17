package cat.uvic.teknos.shoeshop.domain.jpa.models;

import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.Inventory;
import cat.uvic.teknos.shoeshop.models.Model;
import cat.uvic.teknos.shoeshop.models.Shoe;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.*;
public class JpaModelFactory implements ModelFactory{
    @Override
    public Address createAddress() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Address();
    }

    @Override
    public Supplier createSupplier() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Supplier();
    }

    @Override
    public Model createModel() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Model();
    }
    @Override
    public Shoe createShoe() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Shoe();
    }
    @Override
    public ShoeStore createShoeStore() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.ShoeStore();
    }
    @Override
    public Inventory createInventory(){
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Inventory();
    }
    @Override
    public Client createClient() {
        return new cat.uvic.teknos.shoeshop.domain.jpa.models.Client();
    }
}