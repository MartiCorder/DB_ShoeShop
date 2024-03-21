package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Shoe;

import java.util.Set;

public interface ShoeRepository extends Repository<Integer, Shoe>{
    @Override
    void save(Shoe model);

    @Override
    void delete(Shoe model);

    @Override
    Shoe get(Integer id);

    @Override
    Set<Shoe> getAll();
}
