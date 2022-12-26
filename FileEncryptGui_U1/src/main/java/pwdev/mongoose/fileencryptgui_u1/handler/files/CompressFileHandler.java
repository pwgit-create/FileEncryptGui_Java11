package pwdev.mongoose.fileencryptgui_u1.handler.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressFileHandler {

    public CompressFileHandler() {
    }



    public void ArchiveDirectory(String dirName, String nameZipFile) throws IOException {
        ZipOutputStream zip = null;
        FileOutputStream fW = null;
        fW = new FileOutputStream(nameZipFile);
        zip = new ZipOutputStream(fW);
        AddDirectoryToArchive("", dirName, zip);
        zip.close();
        fW.close();
    }

    private void AddDirectoryToArchive(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.list().length == 0) {
            AddFileToArchive(path , srcFolder, zip, true);
        }
        else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    AddFileToArchive(folder.getName(), srcFolder + File.separator + fileName, zip, false);
                }
                else {
                    AddFileToArchive(path + File.separator + folder.getName(), srcFolder + File.separator + fileName, zip, false);
                }
            }
        }
    }

    private void AddFileToArchive(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        File folder = new File(srcFile);
        if (flag) {
            zip.putNextEntry(new ZipEntry(path +File.separator +folder.getName() + File.separator));
        }
        else {
            if (folder.isDirectory()) {
                AddDirectoryToArchive(path, srcFile, zip);
            }
            else {
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }

                in.close();
            }

        }
    }
}


