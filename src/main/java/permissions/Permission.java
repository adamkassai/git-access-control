package permissions;


import java.io.File;
import java.util.HashMap;

public final class Permission {

    private static PermissionType type;
    private static HashMap<File, PermissionType> filePermissions = new HashMap<File, PermissionType>();
    private static HashMap<File, HashMap<Integer, PermissionType>> csvPermissions = new HashMap<File, HashMap<Integer, PermissionType>>();

    public static void set(File file, PermissionType permission) {
        filePermissions.put(file, permission);
    }

    public static void setColumn(File csvFile, int column, PermissionType permission) {

        HashMap<Integer, PermissionType> columnPermissions;

        if (csvPermissions.containsKey(csvFile)) {
            columnPermissions = csvPermissions.get(csvFile);
        } else {
            columnPermissions = new HashMap<Integer, PermissionType>();
        }

        columnPermissions.put(column, permission);
        csvPermissions.put(csvFile, columnPermissions);
    }

    public static boolean isReadable(File file) {
        if (filePermissions.containsKey(file)) {
            return !(filePermissions.get(file) == PermissionType.NONE);
        }
        return true;
    }

    public static boolean isReadableColumn(File csvFile, int column) {

        if (filePermissions.get(csvFile) == PermissionType.NONE) {
            return false;
        }

        if (csvPermissions.containsKey(csvFile) && csvPermissions.get(csvFile).get(column) == PermissionType.NONE) {
            return false;
        }

        return true;
    }

    public static boolean isWriteable(File file) {
        if (filePermissions.containsKey(file)) {
            return (filePermissions.get(file) == PermissionType.FULL);
        }
        return true;
    }

    public static boolean isWriteableColumn(File csvFile, int column) {

        if (filePermissions.containsKey(csvFile) && filePermissions.get(csvFile) != PermissionType.FULL) {
            return false;
        }

        if (csvPermissions.containsKey(csvFile) && csvPermissions.get(csvFile).containsKey(column) && csvPermissions.get(csvFile).get(column) != PermissionType.FULL) {
            return false;
        }

        return true;
    }

}
