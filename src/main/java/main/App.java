package main;


import exceptions.IllegalEditException;
import exceptions.IllegalRowNumberException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import permissions.Permission;
import permissions.PermissionType;

import java.io.File;
import java.io.IOException;

public class App {


    public static void main(String[] args) {

        args = new String[] {"get", "./git_tmp_gold", "./git_tmp_reduced", "gold", "reduced"};

        if (args.length >= 5) {

            try {

                String sourceRemote = args[3];
                String destinationRemote = args[4];
                String lensDirection = args[0];
                String sourceFolder = args[1];
                String destinationFolder = args[2];

                Repository sourceRepo = new FileRepositoryBuilder().setGitDir(new File(sourceFolder + File.separator + ".git")).build();
                Git sourceGit = new Git(sourceRepo);
                sourceGit.pull().setRemote(sourceRemote).call();

                Permission.set(new File(sourceFolder + File.separator + ".git"), PermissionType.NONE);
                Permission.set(new File(sourceFolder + File.separator + "proba2.csv"), PermissionType.NONE);
                Permission.set(new File(sourceFolder + File.separator + "proba3.csv"), PermissionType.READONLY);
                Permission.set(new File(sourceFolder + File.separator + "proba4.csv"), PermissionType.FULL);

                Permission.setColumn(new File(sourceFolder + File.separator + "proba.csv"), 1, PermissionType.NONE);
                Permission.setColumn(new File(sourceFolder + File.separator + "proba.csv"), 3, PermissionType.NONE);
                Permission.setColumn(new File(sourceFolder + File.separator + "proba.csv"), 4, PermissionType.NONE);
                Permission.setColumn(new File(sourceFolder + File.separator + "proba.csv"), 0, PermissionType.FULL);
                Permission.setColumn(new File(sourceFolder + File.separator + "proba.csv"), 2, PermissionType.READONLY);

                FolderLens changeFolder = new FolderLens(sourceFolder, destinationFolder, lensDirection);
                changeFolder.modifyFiles();

                Repository destinationRepo = new FileRepositoryBuilder().setGitDir(new File(destinationFolder + File.separator + ".git")).build();
                Git destinationGit = new Git(destinationRepo);
                destinationGit.add().addFilepattern(".").call();
                destinationGit.commit().setMessage("New commit").call();
                destinationGit.push().setRemote(destinationRemote).call();

            }  catch (IllegalEditException e) {
                e.printStackTrace();
            } catch (IllegalRowNumberException e) {
                e.printStackTrace();
            } catch (CanceledException e) {
                e.printStackTrace();
            } catch (TransportException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }





    }


}
