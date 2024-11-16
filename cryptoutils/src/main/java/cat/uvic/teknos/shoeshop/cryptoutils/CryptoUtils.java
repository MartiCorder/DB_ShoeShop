package cat.uvic.teknos.shoeshop.cryptoutils;

import cat.uvic.teknos.shoeshop.cryptoutils.exeptions.CryptoException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtils {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static String getHash(String text) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(text.getBytes());

            return toBase64(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException();
        }
    }

    public static SecretKey createSecretKey() {
        try {
            return KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static SecretKey decodeSecretKey(String base64SecretKey) {
        var bytes = decoder.decode(base64SecretKey);

        return new SecretKeySpec(bytes, 0, bytes.length, "AES");
    }

    public static String encrypt(String plainText, SecretKey key) {
        return null ; // base64
    }

    public static String decrypt(String encryptedTextBase64, SecretKey key) {
        return null;
    }

    public static String asymmetricEncrypt(String plainTextBase64, Key key) {
        return null;
    }

    public static String asymmetricDecrypt(String encryptedTextBase64, Key key) {
        return null;
    }

    private static String toBase64(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    private static byte[] fromBase64(String base64) {
        return decoder.decode(base64);
    }
}
