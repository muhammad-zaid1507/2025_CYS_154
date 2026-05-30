import java.util.Scanner;

public class Lab5_task4 {
    public static void main(String[] args) {
        Scanner zee = new Scanner(System.in);

        System.out.print("Enter Total marks: ");
        double total = zee.nextDouble();

        System.out.print("Enter Obtained marks: ");
        double obtain = zee.nextDouble();

        simp_per calc = new simp_per(total, obtain);
        System.out.println("Your percentage is: " + calc.getFinalResult());
    }
}

class simp_per {
    private double T_marks;
    private double O_marks;
    private double result;

    public simp_per(double totalMarks, double obtainedMarks) {
        this.T_marks = T_marks;
        this.O_marks = O_marks;
        this.result = (O_marks / T_marks)*100;
    }

    public double getFinalResult() {
        return result;
    }
}
