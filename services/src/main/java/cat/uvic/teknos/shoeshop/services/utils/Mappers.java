package cat.uvic.teknos.shoeshop.services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ShoeStore;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule addressTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Address.class, cat.uvic.teknos.shoeshop.file.models.Address.class)
                .addAbstractTypeMapping(ShoeStore.class, cat.uvic.teknos.shoeshop.file.models.ShoeStore.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(addressTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}