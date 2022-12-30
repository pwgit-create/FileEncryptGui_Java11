package pwdev.mongoose.fileencryptgui_u1.encrypt.threads;


import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pwdev.mongoose.fileencryptgui_u1.encrypt.crypto.AppKeyGenerator;
import pwdev.mongoose.fileencryptgui_u1.encrypt.crypto.AsymmetricCryptography;
import pwdev.mongoose.fileencryptgui_u1.encrypt.enum_.SecurityAction;
import pwdev.mongoose.fileencryptgui_u1.encrypt.util.ArchiveUtil;
import pwdev.mongoose.fileencryptgui_u1.encrypt.util.FileUtil;
import pwdev.mongoose.fileencryptgui_u1.handler.PropHandler;
import pwdev.mongoose.fileencryptgui_u1.handler.files.ByteFileHandler;
import pwdev.mongoose.fileencryptgui_u1.handler.files.CompressFileHandler;
import pwdev.mongoose.fileencryptgui_u1.handler.files.SerFileHandler;


import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class SecurityThread implements Runnable {

    private SecurityAction type;
    private TextArea ta;
    private AsymmetricCryptography ac;

    private Stage stage;
    private PropHandler propHandler;
    private ByteFileHandler fileHandler;
    private CompressFileHandler zipHandler;


    public SecurityThread(SecurityAction type, TextArea ta, Stage stage) {
        this.type = type;
        this.ta = ta;
        this.stage = stage;


        try {
            this.ac = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        propHandler = new PropHandler();
        fileHandler = new ByteFileHandler();
        zipHandler = new CompressFileHandler();

    }

    //#Region Action

    @Override
    public void run() {

        String endUserMsg = "";
        boolean isError = false;
        ta.setText("");

        switch (type) {

            case ENCRYPT:
                EncryptSwitch(isError, endUserMsg);
                break;
            case DECRYPT:
                DecryptSwitch(isError, endUserMsg);
                break;
            case GENERATE_NEW_KEY_PAIR:
                GenerateNewKeyPairSwitch();
                break;
            case SET_PUBLIC_KEY_PATH:
                SetPublicKeySwitch();
                break;
            case SET_PRIVATE_KEY_PATH:
                SetPrivateKeySwitch();

        }
    }

    //#Region Switch case actions

    private void EncryptSwitch(boolean isError, String endUserMsg) {
        byte[] encryptedByteArr = null;

        FileChooser fileChooserEncrypt = new FileChooser();
        fileChooserEncrypt.setTitle("Encrypt File");
        File file = fileChooserEncrypt.showOpenDialog(stage);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file.getAbsoluteFile());
        } catch (FileNotFoundException e) {

            isError = true;
            endUserMsg = "File not found";
            e.printStackTrace();
        }


        byte[] bytesFromInputFile = new byte[(int) file.length()];
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        try {
            dataInputStream.readFully(bytesFromInputFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            dataInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encryptedFilePath = file.getAbsolutePath() + ".enc";

        File outputFile = new File(encryptedFilePath);


        // Placeholder for the outputdir
        Path outputDirPath = null;

        try {

            byte[] ivBytes = generateIv().getIV();

            SecretKeySpec secretKeySpec = new AppKeyGenerator().generateSecretKeySpec();


            ac.encryptFile(file, outputFile, secretKeySpec, ivBytes);


            String s_ivBytes = ac.retrieveEncryptedIvBytes(propHandler.getPublicKeyFromSerFile(), ivBytes);
            String s_secretKeySpec = ac.retrieveEncryptedSymmetricKey(propHandler.getPublicKeyFromSerFile(), secretKeySpec);


            byte[] encSecretKeyArr = s_secretKeySpec.getBytes("UTF-8");
            byte[] encIvBytes = s_ivBytes.getBytes("UTF-8");

            File encryptedSecretKeyFile = new File(file.getAbsolutePath() + fileHandler.SECRET_KEY_FILE_ENDING);
            File encryptedIvFile = new File(file.getAbsolutePath() + fileHandler.Random_IV_BYTES_ENDING);


            fileHandler.writeToFile(encryptedSecretKeyFile, encSecretKeyArr);
            fileHandler.writeToFile(encryptedIvFile, encIvBytes);

            File outputDir = new File(file.getAbsolutePath() + "_enc");

            outputDirPath = outputDir.toPath();


            Path[] filesToDelete = FileUtil.MoveOutputIntoNewDirectory(outputDirPath, outputFile, encryptedSecretKeyFile, encryptedIvFile);


            zipHandler.ArchiveDirectory(outputDir.getAbsolutePath(), outputDirPath.toAbsolutePath().toString() + ".zc");


            FileUtil.RemoveTempOutputDirectory(filesToDelete, outputDir);

        } catch (Exception e) {

            e.printStackTrace();
            isError = true;

        }


        if (!isError)
            endUserMsg = "Created new encrypted file:  " + outputFile.getName();

        ta.setText(endUserMsg);


    }

    private void DecryptSwitch(boolean isError, String endUserMsg) {

        FileChooser fileChooserDecrypt = new FileChooser();
        fileChooserDecrypt.setTitle("Decrypt File");
        File filesToBeExtracted = fileChooserDecrypt.showOpenDialog(stage);

        ArchiveUtil.ExtractArchiveWithEncryptedFiles(filesToBeExtracted);

        String outputDirName = filesToBeExtracted.getName().split(".zc")[0];


        String extractedOutputDirName = filesToBeExtracted.getAbsolutePath().split(".zc")[0];

        File fileToBeDecrypted = ArchiveUtil.GetEncryptedFileAfterZipExtraction(new File(extractedOutputDirName));
        File fileEncryptedSecretKey = ArchiveUtil.GetEncryptedSecurityKeyAfterZipExtraction(new File(extractedOutputDirName));
        File fileEncryptedIvBytes = ArchiveUtil.GetEncryptedIvBytesAfterZipExtraction(new File(extractedOutputDirName));

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileToBeDecrypted.getAbsoluteFile());
        } catch (FileNotFoundException e) {

            isError = true;
            endUserMsg = "File not found";
            e.printStackTrace();
        }


        byte[] bytesFromInputFile = new byte[(int) fileToBeDecrypted.length()];
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        try {
            dataInputStream.readFully(bytesFromInputFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {

            byte[] encryptedSecretKey = fileHandler.ReadFromFile(fileEncryptedSecretKey);
            byte[] encryptedIvBytes = fileHandler.ReadFromFile(fileEncryptedIvBytes);


            String encryptedAesKey = new String(encryptedSecretKey, ac.ENCODING());
            String s_encryptedIvBytes = new String(encryptedIvBytes, ac.ENCODING());


            final SecretKeySpec secretKeySpec = ac.retrieveSymmetricKey(propHandler.getPrivateKeyFromSerFile(), encryptedAesKey);
            final IvParameterSpec ivBytes = ac.retrieveIvBytes(propHandler.getPrivateKeyFromSerFile(), s_encryptedIvBytes);

            if (secretKeySpec == null) {

                isError = true;
                endUserMsg = "Decryption Error!";
            }

            File decryptedOutputFileTmp = new File(FileUtil.GetDecryptedOutputFileName(fileToBeDecrypted.getAbsolutePath()));

            File decryptedOutputFile = FileUtil.RemoveCurrentDirFromPath(decryptedOutputFileTmp);

            ac.decryptFile(fileToBeDecrypted, decryptedOutputFile, secretKeySpec, ivBytes.getIV());

            File outputDir = new File(fileToBeDecrypted.getAbsolutePath());


            Path[] filesInOutputDirectory = new Path[3];

            filesInOutputDirectory[0] = fileToBeDecrypted.toPath();
            filesInOutputDirectory[1] = fileEncryptedSecretKey.toPath();
            filesInOutputDirectory[2] = fileEncryptedIvBytes.toPath();

            fileInputStream.close();

            FileUtil.RemoveTempOutputDirectory(filesInOutputDirectory, fileEncryptedSecretKey.getParentFile());


        } catch (FileNotFoundException fileNotFoundException) {

            isError = true;
            endUserMsg = "File not found";

            fileNotFoundException.printStackTrace();

        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            isError = true;
            endUserMsg = indexOutOfBoundsException.toString();

            indexOutOfBoundsException.printStackTrace();
        } catch (BadPaddingException badPaddingException) {

            isError = true;
            endUserMsg = "Decryption error";

            badPaddingException.printStackTrace();

        } catch (Exception e) {

            isError = true;
            endUserMsg = "error";

            e.printStackTrace();
        }

        if (!isError)
            endUserMsg = "Created new decrypted file:  " + FileUtil.GetDecryptedOutputFileName(fileToBeDecrypted.getAbsolutePath());

        ta.setText(endUserMsg);

    }

    private void GenerateNewKeyPairSwitch() {

        FileChooser fileChooserNewKeyPair = new FileChooser();
        fileChooserNewKeyPair.setTitle("Create new Key pair");
        File fileNewKeyPair = fileChooserNewKeyPair.showSaveDialog(stage);

        SerFileHandler serUtil = new SerFileHandler();

        KeyPair keyPair = null;

        try {
            keyPair = new AppKeyGenerator().generateNewKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        serUtil.WriteSerToFile(keyPair.getPrivate(), fileNewKeyPair.getAbsolutePath() + "_PRIVATE_KEY");
        serUtil.WriteSerToFile(keyPair.getPublic(), fileNewKeyPair.getAbsolutePath() + "_PUBLIC_KEY");

        String outputMsg = "A new keypair has been generated!\nPublic Key: " + keyPair.getPublic() + "\nPrivate Key: " + keyPair.getPrivate();


        ta.setText(outputMsg);

    }

    private void SetPublicKeySwitch() {

        FileChooser fileChooserPublicKey = new FileChooser();
        fileChooserPublicKey.setTitle("Choose public key path");
        File filePublicKey = fileChooserPublicKey.showOpenDialog(stage);

        try {
            propHandler.SetPublicKeyPath(filePublicKey);
            ta.setText("Public key Path: " + filePublicKey.getAbsolutePath());
        } catch (Exception e) {

            e.printStackTrace();
            ta.setText("ERROR: Could not set public key path");
        }

    }

    private void SetPrivateKeySwitch() {


        FileChooser fileChooserPrivateKey = new FileChooser();
        fileChooserPrivateKey.setTitle("Choose private key path");
        File filePrivateKey = fileChooserPrivateKey.showOpenDialog(stage);

        try {
            propHandler.SetPrivateKeyPath(filePrivateKey);
            ta.setText("Private key Path: " + filePrivateKey.getAbsolutePath());
        } catch (Exception e) {

            e.printStackTrace();
            ta.setText("ERROR: Could not set private key path");
        }

    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }


}