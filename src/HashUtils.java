import java.math.BigInteger;
import java.security.MessageDigest;

class HashUtils {

    public static String hashFunc(String number, String plaintext) throws Exception {
        switch (number) {
            case "1":
                number = "MD5";
                break;
            case "2":
                number = "SHA-1";
                break;
            case "3":
                number = "SHA-256";
        }
        MessageDigest m = MessageDigest.getInstance(number);
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}

