package main;

public class App {

    public static void main(String[] args) {

        CSVlens csv = new CSVlens();
        csv.protectedColumns.add(0);
        csv.protectedColumns.add(3);
        csv.protectedColumns.add(7);

        if (args.length!=0) {
            if (args[0].equals("get")) {
                csv.get(args[1], args[2]);
            } else if (args[0].equals("putback")) {
                csv.putback(args[1], args[2]);
            }
        }
    }


}
