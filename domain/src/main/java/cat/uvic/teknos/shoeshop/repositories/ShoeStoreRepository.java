package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.ShoeStore;

import java.util.Set;

public interface ShoeStoreRepository extends Repository<Integer, ShoeStore>{
    @Override
    void save(ShoeStore model);

    @Override
    void delete(ShoeStore model);

    @Override
    ShoeStore get(Integer id);

    @Override
    Set<ShoeStore> getAll();
}
