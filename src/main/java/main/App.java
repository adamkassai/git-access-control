package main;


import java.io.File;

public class App {


    public static void main(String[] args) {

        if (args.length>=3) {

            String lensDirection = args[0];

            FolderLens changeFolder = new FolderLens(args[1], args[2], lensDirection);

            changeFolder.ignoreList.add(new File(args[1] + File.separator + ".git"));
            changeFolder.modifyFiles();

        }


    }


}
