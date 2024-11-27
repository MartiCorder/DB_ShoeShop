import cat.uvic.teknos.shoeshop.cryptoutils.CryptoUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void getHash() {
        var text = "Some text...";
        var base64Text = "quonJ6BjRSC1DBOGuBWNdqixj8z20nuP+QH7cVvp7PI=";

        assertEquals(base64Text, CryptoUtils.getHash(text));
    }

    @Test
    void createSecretKey() {
        var secretKey = CryptoUtils.createSecretKey();

        assertNotNull(secretKey);

        var bytes = secretKey.getEncoded();
        System.out.println(CryptoUtils.toBase64(bytes));
    }

    @Test
    void decodeSecretKey() {
        var secretKeyBase64 = "jaruKzlE7xerbNSjxiVjZtuAeYWrcyMGsA8TaTqZ8AM=";

        var secretKey = CryptoUtils.decodeSecretKey(secretKeyBase64);

        assertNotNull(secretKey);
        assertEquals("AES", secretKey.getAlgorithm());
    }

    @Test
    void encryptAndDecrypt() {
        var secretKey = CryptoUtils.createSecretKey();
        var plainText = "Sensitive data";

        var encryptedText = CryptoUtils.encrypt(plainText, secretKey);
        var decryptedText = CryptoUtils.decrypt(encryptedText, secretKey);

        assertEquals(plainText, decryptedText);
    }

    @Test
    void asymmetricEncryptAndDecrypt() throws Exception {
        var keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        var plainText = "Sensitive asymmetric data";

        var encryptedText = CryptoUtils.asymmetricEncrypt(plainText, keyPair.getPublic());
        var decryptedText = CryptoUtils.asymmetricDecrypt(encryptedText, keyPair.getPrivate());

        assertEquals(plainText, decryptedText);
    }
}
