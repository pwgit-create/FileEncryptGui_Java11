package pwdev.mongoose.fileencryptgui_u1.encrypt.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class AppKeyGenerator extends CryptoBase {


    public AppKeyGenerator() {
    }

    public SecretKeySpec generateSecretKeySpec() throws NoSuchAlgorithmException {

        final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(AES_KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();

        final byte[] aesKey = key.getEncoded();
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, AES);

        return aesKeySpec;
    }

    public KeyPair generateNewKeyPair() throws NoSuchAlgorithmException {


        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);

        keyPairGen.initialize(RSA_KEY_SIZE);


        return keyPairGen.generateKeyPair();

    }
}
