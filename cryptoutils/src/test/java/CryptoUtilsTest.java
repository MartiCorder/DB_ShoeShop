import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptoUtilsTest {

    @Test
    void getHash() {
        var text = "Some text...";
        var base64Text = "quonJ6BjRSC1DBOGuBWNdqixj8z20nuP+QH7cVvp7PI=";

        assertEquals(base64Text, CryptoUtils.getHash(text));

        System.out.println(base64Text);
    }

    @Test
    void createSecretKey() {
    }

    @Test
    void decodeSecretKey() {
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }

    @Test
    void asymmetricEncrypt() {
    }

    @Test
    void asymmetricDecrypt() {
    }
}