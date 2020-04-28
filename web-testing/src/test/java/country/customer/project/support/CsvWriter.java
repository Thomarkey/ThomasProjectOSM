package country.customer.project.support;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;

public class CsvWriter {

    //TODO: change to CSV printer
    public void writeCSV() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("OsmTransferList.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        String ColumnNamesList = "Name;Age;Value;Cost";

        builder.append(ColumnNamesList + "\n");
        String newList = (WorldHelper.getWorld().stringList).toString();
        removeFirstAndLastCharacter(newList);
        String newerList = newList.replace(",", "");
        builder.append((newerList));
//                .append(";");

//        builder.append('\n');
        pw.write(builder.toString());
        pw.close();
        System.out.println("done!");
    }

//    public static void main(String[] args) {
//        PrintWriter pw = null;
//        try {
//            pw = new PrintWriter(new File("NewData4.csv"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        StringBuilder builder = new StringBuilder();
//        String ColumnNamesList = "Name;Value;Cost";
//
//// No need give the headers Like: id, Name on builder.append
//        builder.append(ColumnNamesList + "\n");
//        builder.append("test;test2;test3" + "\n" + "test4;test5;test6");
////                .append(";");
//
//        builder.append('\n');
//        pw.write(builder.toString());
//        pw.close();
//        System.out.println("done!");
//    }

    public static String removeLastCharacter(String str) {
        return StringUtils.substring(str, 0, str.length() - 1);
    }

    public static String removeFirstCharacter(String str) {
        return StringUtils.substring(str, 1, str.length());
    }

    public static String removeFirstAndLastCharacter(String str) {
        return StringUtils.substring(str, 1, str.length() - 1);
    }

    public static void main(String[] args) {
        try (
                CSVPrinter printer = new CSVPrinter(new FileWriter("OSMtransferListTest33.csv", true), CSVFormat.EXCEL)) {

            ArrayList<String> stringList = new ArrayList<>();
            stringList.add("a" + ";" + "v" + ";" + "d" + ";");
            stringList.add("d" + ";" + "s" + ";" + "s" + ";");
            printer.printRecord(stringList);
            printer.printRecord("etstord");
            printer.printRecords(stringList);
            printer.printRecord("hirdghdLTIPLE");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void WriteCSVWithPrinter() {
        try (
                CSVPrinter printer = new CSVPrinter(new FileWriter("OSMTransferList.csv"), CSVFormat.EXCEL)) {
            printer.printRecords(WorldHelper.getWorld().stringList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


