package cat.uvic.teknos.shoeshop.models;

public interface ModelFactory {
    Address createAddress();
    Supplier createSupplier();
    Model createModel();
    Shoe createShoe();
    ShoeStore createShoeStore();
    Inventory createInventory();
    Client createClient();
}