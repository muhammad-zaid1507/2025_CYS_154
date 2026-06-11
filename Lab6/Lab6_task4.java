import java.util.Scanner;
class ScoreCalculator {
    private double fullMarks;
    private double achievedMarks;
    private double score;

    public ScoreCalculator(double fullMarks, double achievedMarks) {
        this.fullMarks = fullMarks;
        this.achievedMarks = achievedMarks;
        this.score = (achievedMarks * 100) / fullMarks;
    }

    public double computedResult() {
        return score;
    }
}
public class Lab6_task4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Total marks: ");
        double fullMarks = sc.nextDouble();

        System.out.print("Enter Obtained marks: ");
        double achievedMarks = sc.nextDouble();

        ScoreCalculator calculator = new ScoreCalculator(fullMarks, achievedMarks);
        System.out.println("Your percentage is: " + calculator.computedResult());
    }
}