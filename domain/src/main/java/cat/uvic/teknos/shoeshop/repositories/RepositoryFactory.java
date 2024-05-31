package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.*;


public interface RepositoryFactory {

    AddressRepository getAddressRepository();

    SupplierRepository getSupplierRepository();

    ModelRepository getModelRepository();
    ClientRepository getClientRepository();
    ShoeRepository getShoeRepository();
    ShoeStoreRepository getShoeStoreRepository();

    InventoryRepository getInventoryRepository();

}
