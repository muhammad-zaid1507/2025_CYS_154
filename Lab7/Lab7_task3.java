import java.io.IOException;
import java.util.ArrayList;

public class Lab7_task3 {

    public static ArrayList<String> studentNames = new ArrayList<>();
    public static ArrayList<String> rollNumbers = new ArrayList<>();
    public static ArrayList<Integer> studentMarks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Lab7_task2.loadCsvData();
        extractData();

        Lab7_task4.calculatePercentages(studentMarks);
        Lab7_task4.displayResults();
    }

    public static void extractData() {
        // Start at index 1 to skip the CSV header row
        for (int i = 1; i < Lab7_task2.csvData.size(); i++) {
            String[] columns = Lab7_task2.csvData.get(i).split(",");
            studentNames.add(columns[0]);
            rollNumbers.add(columns[1]);
            studentMarks.add(Integer.parseInt(columns[2]));
        }
    }

    public static int calculateTotalSum() {
        int total = 0;
        for (int mark : studentMarks) {
            total += mark;
        }
        return total;
    }
}