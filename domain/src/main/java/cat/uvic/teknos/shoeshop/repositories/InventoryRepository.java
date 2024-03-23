package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Inventory;

import java.util.Set;

public interface InventoryRepository extends Repository<Integer, Inventory> {
    @Override
    void save(Inventory model);

    @Override
    void delete(Inventory model);

    @Override
    Inventory get(Integer id);


    @Override
    Set<Inventory> getAll();
}
