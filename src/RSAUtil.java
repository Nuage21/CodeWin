import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;


public class RSAUtil {

    private static String publicKey = "None"; // CodeWin only decrypt - no need for the public key
    public static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAII1f5JWf2CLTINzf33CywH/HtEQF9wIAFEv2UkhLLL+YBWRvn2OrqQAC8gQ7B9rEUvjbNZaHsoIRa4MmFJWdIPT0yp4WW8Edh935Q9xRh4osyz2JOVYsgos4vmZfhVlXH3ky4/BdNRl5rX9vEpmLUZnGL/UoUyOi8I0USoftm0FAgMBAAECgYBaQJ7GXjI4RxWn37y0/PodziocGx2Oo/UkURg5OkdzYiJTkQwKl4Wxo16HNEOWm24HvgHIqfrBau5xb8V8/PQRCpIgK2y0+sxvy45KrkxsHelWpiQt/YfXNCRJWvVSZgUCipl3silJq0BNyV8QCeIWXEdvszHsn9k05TvhCFaRgQJBAPlvYiXb++QWHmwoAXKjzqXe08zK6CFDQGC2uML7OmgdCLB1jxfPkw/vmCEjaG/GpBBi6hhimoBwb3A9DpZ6jPECQQCFos5je1y+sR5ib2VGShElc6DFBeIzjnSzdVCzWlQId0fC+4/RmJCf8uU582SLPBtjVcWJBRzTyocsiNcyw7FVAkA4P8j3VqtwHRjRNRhWfOAuAZjrttowHhVXef0iYzshnWKHQIt7SCEW1+YLmwuDIV2AIH4/74R98F0BB5PRlWNRAkBlieLP6/f4XJP9ry6ATd7hWg4aJfJNwjwh7022OTgIhrlcCdCpLQcWwXQyqCEJOk6FF7LT/bJ9qjEzQXZh+BdRAkADioPRK/v5a6WjEE7vLsaue/H2dcbtgHlEygevi5y49kXR58EuRf8+qXREQtuAhB1UlQEZfkOZvBMFrbeWuqHW";

    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("Hakim Beldjoudi", publicKey));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt("PVQcQ3cC2rJaAxWNSihNIbyVC/W7gktFF62dTvJ23o5fjhwkjQl8uTf01Dp4m8EN3AdQCohTeufF/hYsYJYS25yq4itNOoP5wDqa98kwyxkIcr4AZmDbE8zxlgZKtldLUXEMSjSPuI4ggHISuK0vEpNzUHRfrkfG0eUJavHN1Gs=", privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}
