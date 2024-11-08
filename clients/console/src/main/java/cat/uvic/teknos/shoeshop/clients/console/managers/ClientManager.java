package cat.uvic.teknos.shoeshop.clients.console.managers;

import cat.uvic.teknos.shoeshop.clients.console.dto.ClientDto;
import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;
import cat.uvic.teknos.shoeshop.clients.console.utils.Mappers;
import cat.uvic.teknos.shoeshop.clients.console.utils.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientManager {

    private RestClient restClient;
    private BufferedReader in;

    public ClientManager(RestClient restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void start() throws RequestException, JsonProcessingException {
        String command;
        do {
            showClientMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> {
                    var clients = restClient.getAll("/client", ClientDto[].class);
                    System.out.println("Lista de clientes:");

                    for (ClientDto client : clients) {
                        System.out.println(client.getName());
                    }
                }
                case "2" -> {
                    System.out.print("Ingrese el ID del cliente: ");
                    var clientId = readLine(in);
                    var client = restClient.get("/client/" + clientId, ClientDto.class);
                    System.out.println("Detalles del cliente: " + client.getName());
                }
                case "3" -> {
                    System.out.print("Ingrese el nombre del cliente a agregar: ");
                    var client = new ClientDto();
                    client.setName(readLine(in));
                    restClient.post("/client", Mappers.get().writeValueAsString(client));
                    System.out.println("Cliente agregado exitosamente.");
                }
                case "exit" -> {
                    System.out.println("Saliendo del programa...");
                }
                default -> System.out.println("Comando no válido.");
            }

        } while (!command.equals("exit"));
    }

    private String readLine(BufferedReader in) {
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la opción del menú: " + e);
        }
        return command;
    }

    private void showClientMenu() {
        System.out.println("\n--- Menu de Gestión de Clientes ---");
        System.out.println("1. Lista todos los clientes");
        System.out.println("2. Ver detalles de un cliente");
        System.out.println("3. Agregar un nuevo cliente");
        System.out.println("Type 'exit' to quit.");
    }
}
