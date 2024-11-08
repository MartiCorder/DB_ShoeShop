package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;

public interface RestClient {
    <T> T get(String path, Class<T> returnType) throws RequestException;

    <T> T[] getAll(String path, Class<T[]> returnType) throws RequestException;

    void post(String path, String body) throws RequestException;

    void put(String path, String body) throws RequestException;

    void delete(String path, String body) throws RequestException;
}
