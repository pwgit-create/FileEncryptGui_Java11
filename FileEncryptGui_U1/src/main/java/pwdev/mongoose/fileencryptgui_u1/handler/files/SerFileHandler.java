package pwdev.mongoose.fileencryptgui_u1.handler.files;

import java.io.*;

public class SerFileHandler<T> {

    private String projectPath;

    public SerFileHandler() {

        projectPath = System.getProperty("user.dir");
    }

    public void WriteSerToFile(T serInstance, String writeFilePath) {


        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(writeFilePath + ".ser", true);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serInstance);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public T ReadSerFile(String fileName) {

        ObjectInputStream objectinputstream = null;
        try {
            FileInputStream streamIn = new FileInputStream(fileName);
            objectinputstream = new ObjectInputStream(streamIn);
            T serInstance = (T) objectinputstream.readObject();

            return serInstance;

        } catch (Exception e) {


            e.printStackTrace();
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }


        }
        return null;

    }
}
