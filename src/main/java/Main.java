import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        String message="id=0026219";
        String secret = "1234567890123456789012";

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
        String hash = DatatypeConverter.printHexBinary(bytes);
        System.out.println(hash);
    }
}
