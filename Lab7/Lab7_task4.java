import java.util.ArrayList;

public class Lab7_task4 {

    static ArrayList<Double> percentageList = new ArrayList<>();

    public static void calculatePercentages(ArrayList<Integer> marksList) {
        for (int mark : marksList) {
            double percent = (mark / 30.0) * 100;
            percentageList.add(percent);
        }
    }

    public static void displayResults() {
        for (int i = 0; i < Lab7_task3.studentNames.size(); i++) {
            String name = Lab7_task3.studentNames.get(i);
            String roll = Lab7_task3.rollNumbers.get(i);
            double percent = percentageList.get(i);
            String grade = determineGrade(percent);

            System.out.printf("Name: %s, Roll: %s, Percentage: %.2f%%, Grade: %s%n",
                    name, roll, percent, grade);
        }
    }

    public static String determineGrade(double percentage) {
        if (percentage > 100) {
            throw new IllegalArgumentException("Percentage Higher than 100.");
        }

        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "E";

        return "F";
    }
}