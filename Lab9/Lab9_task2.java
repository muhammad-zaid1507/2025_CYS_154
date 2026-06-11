import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lab9_task2 {

    static ArrayList<Integer> marksList = new ArrayList<>();
    public static List<String> studentNames = new ArrayList<>();
    public static List<String> rollNumbers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Lab9_task1.loadRecords();
        extractMarks();
        extractNames();
        extractRolls();
        ResultCalc.calculatePercentage(marksList);
        ResultCalc.printResults();
    }

    static void computeAverage() {
        // to be implemented
    }

    static int totalSum() {
        int total = 0;
        for (int m : marksList) {
            total = total + m;
        }
        return total;
    }

    static void extractMarks() {
        for (int i = 1; i < Lab9_task1.recordList.size(); i++) {
            marksList.add(Integer.parseInt(Lab9_task1.recordList.get(i).split(",")[2]));
        }
    }

    static void extractNames() {
        for (int i = 1; i < Lab9_task1.recordList.size(); i++) {
            studentNames.add(Lab9_task1.recordList.get(i).split(",")[0]);
        }
    }

    static void extractRolls() {
        for (int i = 1; i < Lab9_task1.recordList.size(); i++) {
            rollNumbers.add(Lab9_task1.recordList.get(i).split(",")[1]);
        }
    }
}