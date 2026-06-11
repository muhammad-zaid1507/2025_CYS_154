import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Lab9_task1 {
    static File csvFile = new File("./StudentData.csv");
    public static ArrayList<String> recordList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        loadRecords();
        for (String entry : recordList) {
            System.out.println(entry);
        }
    }

    static void loadRecords() throws IOException {
        Scanner fileScanner = new Scanner(csvFile);
        while (fileScanner.hasNextLine()) {
            recordList.add(fileScanner.nextLine());
        }
    }
}