import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lab7_task2 {

    static File file = new File("C:\\Users\\hp\\OneDrive\\Desktop\\Jav\\Java\\src\\Book1.csv");
    public static ArrayList<String> csvData = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        loadCsvData();
        for (String row : csvData) {
            System.out.println(row);
        }
    }

    public static void loadCsvData() throws IOException {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                csvData.add(scanner.nextLine());
            }
        }
    }
}