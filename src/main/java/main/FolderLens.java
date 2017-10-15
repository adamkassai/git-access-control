package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class FolderLens {

    List<File> ignoreList = new ArrayList<File>();
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

        if(fileName.lastIndexOf(".")!=0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return "";
    }

    public void modifyFiles() {
        modifyFiles(sourceFolder);
    }


    public void modifyFiles(File folder) {

        for (File file : folder.listFiles()) {

            if (!ignoreList.contains(file)) {

                System.out.println(file.getPath());

                if (file.isDirectory()) {
                    modifyFiles(file);
                } else {
                    String newPath = file.getPath().replace(sourceFolder.getPath(), targetFolder.getPath());
                    new File(newPath).getParentFile().mkdirs();

                    if (getExtension(file).equals("csv")) {

                        CsvLens csv = new CsvLens(file.getPath(), newPath, lensDirection);
                        csv.protectedColumns.add(0);
                        csv.protectedColumns.add(3);
                        csv.protectedColumns.add(7);
                        csv.modifyCsv();

                    }else{
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
