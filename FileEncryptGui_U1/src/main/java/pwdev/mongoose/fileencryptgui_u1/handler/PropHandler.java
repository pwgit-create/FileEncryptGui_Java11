package pwdev.mongoose.fileencryptgui_u1.handler;





import pwdev.mongoose.fileencryptgui_u1.handler.files.SerFileHandler;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;


public class PropHandler {

    private String PROP_FILE_NAME = "keys.properties";

    /**
     * Keys for the values in the properties file
     */

    private final String PRIVATE_KEY_PATH_PROP_VALUE = "PRIVATE_KEY_FILE_PATH";
    private final String PUBLIC_KEY_PATH_PROP_VALUE = "PUBLIC_KEY_FILE_PATH";


    public PropHandler() {

    }

    /**
     * Reads a public key from a ser file
     *
     * @return the public key
     * @throws Exception
     */
    public PublicKey getPublicKeyFromSerFile() throws Exception {


        SerFileHandler<PublicKey> serHandler = new SerFileHandler<>();

        return serHandler.ReadSerFile(readPropertiesFile().getProperty(PUBLIC_KEY_PATH_PROP_VALUE));


    }

    /**
     * Reads a private key from a ser file
     *
     * @return the private key
     * @throws Exception
     */

    public PrivateKey getPrivateKeyFromSerFile() throws Exception {

        SerFileHandler<PrivateKey> serHandler = new SerFileHandler<>();

        return serHandler.ReadSerFile(readPropertiesFile().getProperty(PRIVATE_KEY_PATH_PROP_VALUE));
    }


    /**
     * Read the paths to the Cryptographic keys from the properties file
     *
     * @return
     * @throws IOException
     */
    private Properties readPropertiesFile() throws IOException {


        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(PROP_FILE_NAME);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException fnfe) {

            return null;


        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }

    public void SetPublicKeyPath(File publicKey) throws IOException {

      SetKeyPath(publicKey,PUBLIC_KEY_PATH_PROP_VALUE);

    }

    public void SetPrivateKeyPath(File privateKey) throws IOException {

      SetKeyPath(privateKey,PRIVATE_KEY_PATH_PROP_VALUE);
    }

    private void SetKeyPath(File keyFile,String propKey) throws IOException {


        Properties prop = readPropertiesFile();



        prop.setProperty(propKey,keyFile.getAbsolutePath());

        try (OutputStream output = new FileOutputStream(PROP_FILE_NAME)) {


            prop.store(output, null);


        } catch (IOException io) {
            io.printStackTrace();
        }


    }

}

