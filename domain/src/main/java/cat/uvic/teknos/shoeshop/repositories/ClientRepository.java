package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Client;

import java.util.Set;

public interface ClientRepository extends Repository<Integer, Client>{

    @Override
    void save(Client model);

    @Override
    void delete(Client model);

    @Override
    Client get(Integer id);

    Client get(String name);



    @Override
    Set<Client> getAll();
}
