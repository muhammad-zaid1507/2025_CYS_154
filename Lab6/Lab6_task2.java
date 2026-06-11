import java.util.Scanner;
class MarksEvaluator {
    private double totalScore;
    private double scoredMarks;
    private double percentage;

    public MarksEvaluator(double totalScore, double scoredMarks) {
        this.totalScore = totalScore;
        this.scoredMarks = scoredMarks;
        this.percentage = (scoredMarks * 100) / totalScore;
    }

    public double getPercentage() {
        return percentage;
    }

    public void displayGrade() {
        if (percentage >= 90)      System.out.println("Your grade is: A");
        else if (percentage >= 85) System.out.println("Your grade is: A-");
        else if (percentage >= 80) System.out.println("Your grade is: B+");
        else if (percentage >= 75) System.out.println("Your grade is: B");
        else if (percentage >= 70) System.out.println("Your grade is: B-");
        else if (percentage > 65)  System.out.println("Your grade is: C+");
        else if (percentage > 60)  System.out.println("Your grade is: C");
        else if (percentage > 55)  System.out.println("Your grade is: C-");
        else if (percentage > 50)  System.out.println("Your grade is: D");
        else                       System.out.println("Your grade is: F");
    }
}
public class Lab6_task2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Total marks: ");
        double totalScore = sc.nextDouble();

        System.out.print("Enter Obtained marks: ");
        double scoredMarks = sc.nextDouble();

        MarksEvaluator evaluator = new MarksEvaluator(totalScore, scoredMarks);
        System.out.println("Your Percentage is: " + evaluator.getPercentage());
        evaluator.displayGrade();
    }
}