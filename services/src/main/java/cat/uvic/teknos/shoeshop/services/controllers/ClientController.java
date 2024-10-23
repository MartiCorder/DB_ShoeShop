package cat.uvic.teknos.shoeshop.services.controllers;

import cat.uvic.teknos.shoeshop.models.Client;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.models.Address;
import cat.uvic.teknos.shoeshop.models.ShoeStore;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class ClientController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public ClientController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Client client = repositoryFactory.getClientRepository().get(id);

        if (client == null) {
            return "{\"error\": \"Client not found\"}";
        }

        try {
            return mapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting client to JSON", e);
        }
    }

    @Override
    public String get() {
        var clients = repositoryFactory.getClientRepository().getAll();

        try {
            return mapper.writeValueAsString(clients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting clients list to JSON", e);
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            Client client = new cat.uvic.teknos.shoeshop.file.models.Client();

            if (rootNode.has("dni")) {
                client.setDni(rootNode.get("dni").asText());
            }

            if (rootNode.has("name")) {
                client.setName(rootNode.get("name").asText());
            }

            if (rootNode.has("phone")) {
                client.setPhone(rootNode.get("phone").asText());
            }

            // Gestió d'adreces
            if (rootNode.has("address")) {
                Set<Address> addresses = new HashSet<>();
                JsonNode addressesNode = rootNode.get("address");
                for (JsonNode addressNode : addressesNode) {
                    Address address = new cat.uvic.teknos.shoeshop.file.models.Address();
                    if (addressNode.has("address")) {
                        address.setLocation(addressNode.get("address").asText());
                    }
                    addresses.add(address);
                }
                client.setAddresses((Address) addresses);
            }

            // Gestió de botigues de sabates
            if (rootNode.has("shoe_store")) {
                Set<ShoeStore> shoeStores = new HashSet<>();
                JsonNode shoeStoresNode = rootNode.get("shoe_store");
                for (JsonNode shoeStoreNode : shoeStoresNode) {
                    ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                    if (shoeStoreNode.has("name")) {
                        shoeStore.setName(shoeStoreNode.get("name").asText());
                    }
                    if (shoeStoreNode.has("location")) {
                        shoeStore.setLocation(shoeStoreNode.get("location").asText());
                    }
                    if (shoeStoreNode.has("owner")) {
                        shoeStore.setOwner(shoeStoreNode.get("owner").asText());
                    }
                    shoeStores.add(shoeStore);
                }
                client.setShoeStores((ShoeStore) shoeStores);
            }

            if (client.getDni() == null || client.getName() == null) {
                throw new IllegalArgumentException("DNI and Name are required fields");
            }

            repositoryFactory.getClientRepository().save(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing client from JSON", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid client data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving client", e);
        }
    }

    @Override
    public void put(int id, String json) {
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient == null) {
            throw new RuntimeException("Client not found");
        }

        try {
            JsonNode rootNode = mapper.readTree(json);
            if (rootNode.has("dni")) {
                existingClient.setDni(rootNode.get("dni").asText());
            }

            if (rootNode.has("name")) {
                existingClient.setName(rootNode.get("name").asText());
            }

            if (rootNode.has("phone")) {
                existingClient.setPhone(rootNode.get("phone").asText());
            }

            // Gestió d'adreces
            if (rootNode.has("addresses")) {
                Set<Address> addresses = new HashSet<>();
                JsonNode addressesNode = rootNode.get("addresses");
                for (JsonNode addressNode : addressesNode) {
                    Address address = new cat.uvic.teknos.shoeshop.file.models.Address();
                    if (addressNode.has("address")) {
                        address.setLocation(addressNode.get("address").asText());
                    }
                    addresses.add(address);
                }
                existingClient.setAddresses((Address) addresses);
            }

            // Gestió de botigues de sabates
            if (rootNode.has("shoeStores")) {
                Set<ShoeStore> shoeStores = new HashSet<>();
                JsonNode shoeStoresNode = rootNode.get("shoeStores");
                for (JsonNode shoeStoreNode : shoeStoresNode) {
                    ShoeStore shoeStore = new cat.uvic.teknos.shoeshop.file.models.ShoeStore();
                    if (shoeStoreNode.has("name")) {
                        shoeStore.setName(shoeStoreNode.get("name").asText());
                    }
                    if (shoeStoreNode.has("location")) {
                        shoeStore.setLocation(shoeStoreNode.get("location").asText());
                    }
                    shoeStores.add(shoeStore);
                }
                existingClient.setShoeStores((ShoeStore) shoeStores);
            }

            repositoryFactory.getClientRepository().save(existingClient);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing client from JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Client existingClient = repositoryFactory.getClientRepository().get(id);

        if (existingClient != null) {
            repositoryFactory.getClientRepository().delete(existingClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }
}
