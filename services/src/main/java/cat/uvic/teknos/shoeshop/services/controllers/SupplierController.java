package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Supplier;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import cat.uvic.teknos.shoeshop.repositories.SupplierRepository;
import cat.uvic.teknos.shoeshop.services.utils.Mappers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SupplierController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public SupplierController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Supplier supplier = repositoryFactory.getSupplierRepository().get(id);

        if (supplier == null) {
            return "{\"error\": \"Supplier not found\"}";
        }

        try {
            return mapper.writeValueAsString(supplier);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting supplier to JSON", e);
        }
    }

    @Override
    public String get() {
        var suppliers = repositoryFactory.getSupplierRepository().getAll();

        try {
            return mapper.writeValueAsString(suppliers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting suppliers list to JSON", e);
        }
    }

    @Override
    public void post(String json) {

        SupplierRepository repository = repositoryFactory.getSupplierRepository();

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier supplier = mapper.readValue(json, cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier.class);
            repository.save(supplier);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void put(int id, String json) {

        SupplierRepository repository = repositoryFactory.getSupplierRepository();
        Supplier existingSupplier = repository.get(id);

        if (existingSupplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        ObjectMapper mapper = Mappers.get();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            Supplier supplierUpdated = mapper.readValue(json,cat.uvic.teknos.shoeshop.domain.jdbc.models.Supplier.class);
            supplierUpdated.setId(id);

            existingSupplier.setName(supplierUpdated.getName());
            existingSupplier.setPhone(supplierUpdated.getPhone());
            existingSupplier.setShoeStores(supplierUpdated.getShoeStores());

           repository.save(existingSupplier);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing supplier from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Supplier existingSupplier = repositoryFactory.getSupplierRepository().get(id);

        if (existingSupplier != null) {
            repositoryFactory.getSupplierRepository().delete(existingSupplier);
        } else {
            throw new RuntimeException("Supplier not found");
        }
    }
}
