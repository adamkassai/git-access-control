package main;

import permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class FolderLens {

    private String lensDirection;
    private File sourceFolder;
    private File targetFolder;

    public FolderLens(String sourceFolder, String targetFolder, String lensDirection) {
        this.lensDirection = lensDirection;
        this.targetFolder = new File(targetFolder);
        this.sourceFolder = new File(sourceFolder);
    }

    public String getExtension(File file) {

        String fileName = file.getName();

        if (fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return "";
    }

    public void modifyFiles() {
        modifyFiles(sourceFolder);
    }


    public void modifyFiles(File folder) {

        for (File file : folder.listFiles()) {

            if ((lensDirection.equals("get") && Permission.isReadable(file))
                    || (lensDirection.equals("putback") && Permission.isWriteable(file))) {

                if (file.isDirectory()) {
                    modifyFiles(file);
                } else {
                    String newPath = file.getPath().replace(sourceFolder.getPath(), targetFolder.getPath());
                    new File(newPath).getParentFile().mkdirs();

                    if (getExtension(file).equals("csv")) {

                        CsvLens csv = new CsvLens(file.getPath(), newPath, lensDirection);
                        csv.modifyCsv();

                    } else {
                        try {
                            Files.copy(file.toPath(), new File(newPath).toPath(), REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }
    }

}
