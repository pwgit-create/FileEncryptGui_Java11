package pwdev.mongoose.fileencryptgui_u1.encrypt.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Arrays;

public class ArchiveUtil {

    public static boolean ExtractArchiveWithEncryptedFiles(File archiveWithEncryptedFiles) {

        ZipFile zipFile = new ZipFile((archiveWithEncryptedFiles));

        try {
            zipFile.extractAll(archiveWithEncryptedFiles.getParent());
        } catch (ZipException e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    /**
     * Retrieves the encrypted file from the archive
     * @param outputDirOfExtractedArchive
     * @return
     */
    public static File GetEncryptedFileAfterZipExtraction(File outputDirOfExtractedArchive) {

        System.out.println(outputDirOfExtractedArchive.getAbsolutePath());

        Arrays.stream(outputDirOfExtractedArchive.listFiles()).sequential().forEach(f -> System.out.println(f.getAbsolutePath()));

        return Arrays.stream(outputDirOfExtractedArchive.listFiles()).sequential().filter(f -> f.getAbsolutePath().endsWith(".enc")).findAny().get();

    }

    /**
     * Retrieves the encrypted security key from the archive
     * @param outputDirOfExtractedArchive
     * @return
     */
    public static File GetEncryptedSecurityKeyAfterZipExtraction(File outputDirOfExtractedArchive) {

        return Arrays.stream(outputDirOfExtractedArchive.listFiles()).sequential().filter(f -> f.getAbsolutePath().endsWith(".secret_key")).findAny().get();

    }

    /**
     * Retrieves the encrypted IV bytes from the archive
     * @param outputDirOfExtractedArchive
     * @return
     */
    public static File GetEncryptedIvBytesAfterZipExtraction(File outputDirOfExtractedArchive) {

        return Arrays.stream(outputDirOfExtractedArchive.listFiles()).sequential().filter(f -> f.getAbsolutePath().endsWith(".iv")).findAny().get();

    }
}
