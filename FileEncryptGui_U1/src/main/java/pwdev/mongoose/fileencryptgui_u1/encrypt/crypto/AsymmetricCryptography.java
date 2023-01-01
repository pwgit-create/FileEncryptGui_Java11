package pwdev.mongoose.fileencryptgui_u1.encrypt.crypto;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;


public class AsymmetricCryptography extends CryptoBase {

    private final String FILE_CRYPT_ALGORITHM = "AES/CFB8/NoPadding";

    private final String ENCODING = "UTF-8";

    private final int ioStreamBufferSize = 1024 * 16;

    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
    }

    public String ENCODING() {
        return ENCODING;
    }


    public String encryptIvBytes(PublicKey pubKey, byte[] ivBytes) {

        try {

            final String encodedKey = Base64.encodeBase64String(ivBytes);
            final byte[] plainBytes = encodedKey.getBytes(ENCODING);
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            final byte[] encrypted = cipher.doFinal(plainBytes);
            String encryptedString = new String(Base64.encodeBase64(encrypted));

            return encryptedString;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IvParameterSpec decryptIvBytes(PrivateKey privateKey, String cipherText) {
        IvParameterSpec ivParameterSpec = null;
        try {
            final byte[] plainBytes = Base64.decodeBase64(cipherText.getBytes(ENCODING));
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] decrypted = cipher.doFinal(plainBytes);
            final String DecryptedString = new String(decrypted, ENCODING);

            byte[] decodedIvBytes = Base64.decodeBase64(DecryptedString);
            ivParameterSpec = new IvParameterSpec(decodedIvBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return ivParameterSpec;
    }


    public boolean encryptFile(final File in, final File out, final SecretKeySpec secretKey, byte[] ivBytes) {


        try {

            final Cipher cipher = Cipher.getInstance(FILE_CRYPT_ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));

            BufferedInputStream is = new BufferedInputStream(new FileInputStream(in), ioStreamBufferSize);

            BufferedOutputStream osb = new BufferedOutputStream(new FileOutputStream(out), ioStreamBufferSize);
            CipherOutputStream os = new CipherOutputStream(osb, cipher);


            copyBytes(is, os);


            is.close();
            osb.flush();
            osb.close();
            os.flush();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean decryptFile(final File in, final File out, final SecretKeySpec secretKeySpec,
                               byte[] ivBytes) {

        try {

            final Cipher cipher = Cipher.getInstance(FILE_CRYPT_ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));

            CipherInputStream is = new CipherInputStream(new FileInputStream(in), cipher);
            BufferedInputStream bif = new BufferedInputStream(is, ioStreamBufferSize);
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out), ioStreamBufferSize);

            copyBytes(bif, os);
            is.close();
            bif.close();
            os.close();


            copyBytes(is, os);

            is.close();

            bif.close();
            os.flush();
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public SecretKeySpec retrieveSymmetricKey(PrivateKey privateKey, String encryptedAesKey) {

        return decryptSecretKey(privateKey, encryptedAesKey);
    }


    public String retrieveEncryptedSymmetricKey(PublicKey publicKey, SecretKeySpec secretKeySpec) {
        return encryptSecretKey(publicKey, secretKeySpec);
    }

    public IvParameterSpec retrieveIvBytes(PrivateKey privateKey, String encryptedIvBytes) {

        return decryptIvBytes(privateKey, encryptedIvBytes);
    }


    public String retrieveEncryptedIvBytes(PublicKey publicKey, byte[] ivBytes) {

        return encryptIvBytes(publicKey, ivBytes);
    }

    private String encryptSecretKey(PublicKey pubKey, SecretKeySpec aesKeySpec) {

        try {

            final String encodedKey = Base64.encodeBase64String(aesKeySpec.getEncoded());
            final byte[] plainBytes = encodedKey.getBytes(ENCODING);
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            final byte[] encrypted = cipher.doFinal(plainBytes);
            String encryptedString = new String(Base64.encodeBase64(encrypted));

            return encryptedString;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SecretKeySpec decryptSecretKey(PrivateKey privateKey, String cipherText) {
        SecretKeySpec aesKeySpec = null;
        try {
            final byte[] plainBytes = Base64.decodeBase64(cipherText.getBytes(ENCODING));
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] decrypted = cipher.doFinal(plainBytes);
            final String DecryptedString = new String(decrypted, ENCODING);

            byte[] decodedKey = Base64.decodeBase64(DecryptedString);
            aesKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
            return aesKeySpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void copyBytes(InputStream is, OutputStream os) throws IOException {
        int i;
        final byte[] b = new byte[8192];
        while ((i = is.read(b)) != -1) {
            os.write(b, 0, i);
            os.flush();
        }
        os.close();
        is.close();
    }

}
