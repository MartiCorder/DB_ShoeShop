package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule addressTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Address.class, AddressDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Inventory.class, InventoryDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Model.class, ModelDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Client.class, ClientDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Shoe.class, ShoeDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.Supplier.class, SupplierDto.class)
                .addAbstractTypeMapping(cat.uvic.teknos.shoeshop.models.ShoeStore.class, ShoeStoreDto.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(addressTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}