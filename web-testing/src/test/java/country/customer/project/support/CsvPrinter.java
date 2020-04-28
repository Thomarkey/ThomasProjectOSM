package country.customer.project.support;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class CsvPrinter {

    public void printCsv() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter("csv.txt"), CSVFormat.EXCEL)) {
            printer.printRecord("Name", "Cost", "Value");
            printer.printRecord("1,2", "john73", "John", "Doe", LocalDate.of(1973, 9, 15));
            printer.printRecord(2, "mary", "Mary", "Meyer", LocalDate.of(1985, 3, 29));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try (CSVPrinter printer = new CSVPrinter(new FileWriter("csv.txt"), CSVFormat.EXCEL)) {
            printer.printRecord("id", "userName", "firstName", "lastName", "birthday");
            printer.printRecord("1,2", "john73", "John", "Doe", LocalDate.of(1973, 9, 15));
            printer.printRecord(2, "mary", "Mary", "Meyer", LocalDate.of(1985, 3, 29));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
