import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {

    private static final String SECRET_KEY = "SistemasDistrib!";

    public static String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            System.err.println("Erro ao encriptar: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.err.println("Erro ao desencriptar: " + e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        String original = "Olá Mundo Distribuído";
        String encriptado = encrypt(original);
        String decriptado = decrypt(encriptado);

        System.out.println("Original: " + original);
        System.out.println("Encriptado: " + encriptado);
        System.out.println("Decriptado: " + decriptado);
    }
}