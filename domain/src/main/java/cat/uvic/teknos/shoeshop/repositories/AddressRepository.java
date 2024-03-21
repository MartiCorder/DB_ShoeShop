package cat.uvic.teknos.shoeshop.repositories;

import cat.uvic.teknos.shoeshop.models.Address;

import java.util.Set;

public interface AddressRepository extends Repository<Integer, Address> {

    @Override
    void save(Address model);

    @Override
    void delete(Address model);

    @Override
     Address get(Integer id);

    @Override
    Set<Address> getAll();

}
