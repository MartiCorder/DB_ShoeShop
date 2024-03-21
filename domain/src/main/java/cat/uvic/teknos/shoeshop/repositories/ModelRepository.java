package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Model;

import java.util.Set;

public interface ModelRepository extends Repository<Integer, Model>{
    @Override
    void save(Model model);

    @Override
    void delete(Model model);

    @Override
    Model get(Integer id);

    @Override
    Set<Model> getAll();
}
