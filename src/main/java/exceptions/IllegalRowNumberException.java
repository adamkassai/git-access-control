package exceptions;

/**
 * Created by Adam on 2017.10.30..
 */
public class IllegalRowNumberException extends Exception {
    public IllegalRowNumberException(String srcFile) {
        super("You can't modify the number of rows in " + srcFile);
    }
}
