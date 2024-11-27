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

    @FunctionalInterface
    interface BodyDecoder {
        String decode(String body);
    }

    interface BodyEncoder {
        String encode(String body);
    }


    <T> T get(String path, Class<T> returnType, BodyDecoder decoder, HeaderEntry... entries) throws RequestException;

    <T> T[] getAll(String path, Class<T[]> returnType, BodyDecoder decoder, HeaderEntry... entries) throws RequestException;

    void post(String path, String body, BodyEncoder encoder, HeaderEntry... entries) throws RequestException;

    void put(String path, String body, BodyEncoder encoder, HeaderEntry... entries) throws RequestException;

    void delete(String path, String body, HeaderEntry... entries) throws RequestException;
}
