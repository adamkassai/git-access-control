package main;


import permissions.Permission;
import permissions.PermissionType;

import java.io.File;

public class App {


    public static void main(String[] args) {

        if (args.length >= 3) {

            String lensDirection = args[0];

            Permission.set(new File(args[1] + File.separator + ".git"), PermissionType.NONE);
            Permission.set(new File(args[1] + File.separator + "proba2.csv"), PermissionType.NONE);
            Permission.set(new File(args[1] + File.separator + "proba3.csv"), PermissionType.READONLY);
            Permission.set(new File(args[1] + File.separator + "proba4.csv"), PermissionType.FULL);

            Permission.setColumn(new File(args[1] + File.separator + "proba.csv"), 1, PermissionType.NONE);
            Permission.setColumn(new File(args[1] + File.separator + "proba.csv"), 3, PermissionType.NONE);
            Permission.setColumn(new File(args[1] + File.separator + "proba.csv"), 4, PermissionType.NONE);
            Permission.setColumn(new File(args[1] + File.separator + "proba.csv"), 0, PermissionType.FULL);
            Permission.setColumn(new File(args[1] + File.separator + "proba.csv"), 2, PermissionType.READONLY);

            FolderLens changeFolder = new FolderLens(args[1], args[2], lensDirection);
            changeFolder.modifyFiles();

        }


    }


}
