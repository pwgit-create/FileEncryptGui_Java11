package pwdev.mongoose.fileencryptgui_u1.handler.files;

import java.io.*;

public class ByteFileHandler {

    public final String SECRET_KEY_FILE_ENDING=".secret_key";
    public final String Random_IV_BYTES_ENDING=".iv";


    public ByteFileHandler() {
    }


    public void writeToFile(File output, byte[] toWrite)
        {

            try {
                FileOutputStream fos = new FileOutputStream(output);
                fos.write(toWrite);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public byte[] ReadFromFile(File input){

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(input.getAbsoluteFile());
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }


        byte[] bytesFromInputFile = new byte[(int) input.length()];
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        try {
            dataInputStream.readFully(bytesFromInputFile);
            return bytesFromInputFile;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        finally {
            try {
                fileInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                dataInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        return null;
    }
}
