package pwdev.mongoose.fileencryptgui_u1.encrypt.util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileUtil {

    public static void AddLockOnPath(Path path) {

        FileChannel channel;
        try {
            channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            System.out.println("lock is valid: -> " + channel.tryLock().isValid());
            if (!channel.tryLock().isValid()) {

                channel.lock();

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static void AddLockOnPathArr(Path[] pathArr) {

        Arrays.stream(pathArr).forEach(p -> AddLockOnPath(p));
    }

    public static void ReleaseLockOnPath(Path path) {

        FileChannel channel;
        try {
            channel = FileChannel.open(path.toAbsolutePath(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            System.out.println("lock is valid: -> " + channel.tryLock().isValid());
            if (channel.lock().isValid()) {


                channel.lock().close();

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    public static void ReleaseLockOnPathArr(Path[] pathArr) {

        Arrays.stream(pathArr).forEach(p -> ReleaseLockOnPath(p));
    }


    /**
     * Remove the occurrence of the last ".enc" from the String
     *
     * @param decryptFileName
     * @return
     */
    public static String GetDecryptedOutputFileName(String decryptFileName) {

        StringBuilder sb = new StringBuilder();

        String removeFromString = ".enc";

        int startRemoveIndex = decryptFileName.lastIndexOf(removeFromString);

        String originalFileName = decryptFileName.substring(0, startRemoveIndex);

        sb.append(originalFileName);

        return sb.toString();


    }

    public static File RemoveCurrentDirFromPath(File file) {

        String fileName = file.getName();

        System.out.println("end path -> " + file.toPath().getParent().getParent().toAbsolutePath().resolve(fileName).toFile());

        return file.toPath().getParent().getParent().toAbsolutePath().resolve(fileName).toFile();
    }

    public static Path[] MoveOutputIntoNewDirectory(Path directoryPath, File encryptedFile, File encryptedAesKey,
                                                    File encryptedIvBytesFile) {

        Path[] newPaths = new Path[3];


        try {


            Files.createDirectory(directoryPath);


        } catch (IOException ioException) {
            ioException.printStackTrace();

        }

        try {


            newPaths[0] = Files.move(encryptedFile.getAbsoluteFile().toPath(), directoryPath.resolve(encryptedFile.toPath().getFileName()));
            newPaths[1] = Files.move(encryptedAesKey.toPath(), directoryPath.resolve(encryptedAesKey.toPath().getFileName()));
            newPaths[2] = Files.move(encryptedIvBytesFile.toPath(),directoryPath.resolve(encryptedIvBytesFile.toPath().getFileName()));


        } catch (IOException ioException) {
            ioException.printStackTrace();

        }


        return newPaths;

    }

    /**
     * Removes the temporary output directory
     *
     * @param filesToDelete
     * @param outputDir
     * @return
     */
    public static boolean RemoveTempOutputDirectory(Path[] filesToDelete, File outputDir) {


        if (filesToDelete.length == 3) {

            try {

                Files.delete(filesToDelete[0]);

                Files.delete(filesToDelete[1]);

                Files.delete(filesToDelete[2]);

                Files.delete(outputDir.toPath());

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }
        return true;
    }
}
