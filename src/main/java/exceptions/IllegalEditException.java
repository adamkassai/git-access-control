package exceptions;

public class IllegalEditException extends Exception {

    public IllegalEditException(String srcFile, int row, int column) {
        super("You are not allowed to edit cell " + row + ":" + column + " in " + srcFile);
    }
}
