package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Client;

import java.util.Set;

public interface ClientRepository extends Repository<Integer, Client>{

    Client get(String name);
}
