import java.util.ArrayList;

class ResultCalc {

    static ArrayList<Double> percentageList = new ArrayList<>();

    public static void calculatePercentage(ArrayList<Integer> marksList) {
        for (int m : marksList) {
            percentageList.add(m / 30.0 * 100);
        }
    }

    public static void printResults() {
        for (int i = 0; i < Lab9_task1.recordList.size() - 1; i++) {
            System.out.print(
                    "Name: " + Lab9_task2.studentNames.get(i) + ", " +
                            "Roll: " + Lab9_task2.rollNumbers.get(i) + ", " +
                            "Percentage: " + String.format("%.2f", percentageList.get(i)) + "%, " +
                            "Grade: " + evaluateGrade(percentageList.get(i))
            );
            System.out.println();
        }
    }

    static String evaluateGrade(double percentage) {
        if (!(percentage > 100)) {
            if (percentage >= 90)      return "A";
            else if (percentage >= 80) return "B";
            else if (percentage >= 70) return "C";
            else if (percentage >= 60) return "E";
            else                       return "F";
        } else {
            throw new RuntimeException("Percentage exceeds 100.");
        }
    }
}