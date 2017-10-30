package main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import exceptions.IllegalEditException;
import exceptions.IllegalRowNumberException;
import permissions.Permission;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvLens {

    private String lensDirection;
    private String sourceFile;
    private String targetFile;

    public CsvLens(String sourceFile, String targetFile, String lensDirection) {
        this.lensDirection = lensDirection;
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    public void modifyCsv() throws IllegalEditException, IllegalRowNumberException {
        if (lensDirection.equals("get")) {
            get(sourceFile, targetFile);
        } else if (lensDirection.equals("putback")) {
            putback(sourceFile, targetFile);
        }
    }

    private List<String[]> readCSV(String srcFile) {

        List<String[]> rows = new ArrayList<String[]>();

        CSVReader reader = null;

        try {

            reader = new CSVReader(new FileReader(srcFile));

            String[] row;

            while ((row = reader.readNext()) != null) {
                rows.add(row);
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return rows;
    }

    private void writeCSV(List list, String dstFile) {

        CSVWriter writer = null;

        try {

            writer = new CSVWriter(new FileWriter(dstFile));
            writer.writeAll(list);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void get(String srcFile, String dstFile) {

        List<String[]> list = readCSV(srcFile);
        List<String[]> newList = new ArrayList<String[]>();


        for (String[] row : list) {

            List<String> newRow = new ArrayList<String>();

            for (int i = 0; i < row.length; i++) {
                if (Permission.isReadableColumn(new File(srcFile), i)) {
                    newRow.add(row[i]);
                }
            }

            newList.add(newRow.toArray(new String[0]));

        }

        writeCSV(newList, dstFile);

    }

    public void putback(String srcFile, String dstFile) throws IllegalRowNumberException, IllegalEditException {

        List<String[]> list = readCSV(srcFile);
        List<String[]> originalList = readCSV(dstFile);
        List<String[]> newList = new ArrayList<String[]>();


            if (list.size() != originalList.size()) {
                throw new IllegalRowNumberException(srcFile);
            }

            for (int i = 0; i < list.size(); i++) {

                String[] row = list.get(i);
                String[] originalRow = originalList.get(i);

                List<String> newRow = new ArrayList<String>();

                int newIndex = 0;

                for (int j = 0; j < originalRow.length; j++) {

                    if (!Permission.isReadableColumn(new File(srcFile), j)) {
                        newRow.add(originalRow[j]);
                    } else if (!Permission.isWriteableColumn(new File(srcFile), j)) {
                        if (!originalRow[j].equals(row[newIndex])) {
                            throw new IllegalEditException(srcFile, i, newIndex);
                        }
                        newRow.add(originalRow[j]);
                        newIndex++;
                    } else {
                        newRow.add(row[newIndex]);
                        newIndex++;
                    }
                }


                newList.add(newRow.toArray(new String[0]));

            }

            writeCSV(newList, dstFile);





    }

}