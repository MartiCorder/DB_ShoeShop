package cat.uvic.teknos.shoeshop.clients.console.utils;

import cat.uvic.teknos.shoeshop.clients.console.exceptions.RequestException;

public interface RestClient {

    class HeaderEntry {
        public String key;
        public String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public HeaderEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    <T> T get(String path, Class<T> returnType) throws RequestException;

    <T> T[] getAll(String path, Class<T[]> returnType) throws RequestException;

    void post(String path, String body) throws RequestException;

    void put(String path, String body) throws RequestException;

    void delete(String path, String body) throws RequestException;
}