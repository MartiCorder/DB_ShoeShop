package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Supplier;

import java.util.Set;

public interface SupplierRepository extends Repository<Integer, Supplier>{
    @Override
    void save(Supplier model);

    @Override
    void delete(Supplier model);

    @Override
    Supplier get(Integer id);

    @Override
    Set<Supplier> getAll();
}
