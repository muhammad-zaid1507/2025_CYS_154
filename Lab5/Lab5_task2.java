import java.util.Scanner;

public class Lab5_task2 {
    public static void main(String[] args) {
        Scanner zee = new Scanner(System.in);

        System.out.print("Enter T_marks: ");
        double t_marks =zee.nextDouble();

        System.out.print("Enter O_marks: ");
        double o_marks = zee.nextDouble();

        GradeCalculator calc = new GradeCalculator(t_marks, o_marks);
        System.out.println("Your Per : " + calc.getPer());
        calc.printGrade();

    }
}

class GradeCalculator {
    private double t_marks;
    private double o_marks;
    private double per;

    public GradeCalculator(double t_marks, double o_marks) {
        this.t_marks = t_marks;
        this.o_marks = o_marks;
        this.per = (o_marks * 100) / t_marks;
    }

    public double getPer() {
        return per;
    }

    public void printGrade() {
        if (per >= 90) System.out.println("Your grade: A");
        else if (per >= 85) System.out.println("Your grade: A-");
        else if (per >= 80) System.out.println("Your grade: B+");
        else if (per >= 75) System.out.println("Your grade: B");
        else if (per >= 70) System.out.println("Your grade: B-");
        else if (per > 65) System.out.println("Your grade: C+");
        else if (per > 60) System.out.println("Your grade: C");
        else if (per > 55) System.out.println("Your grade: C-");
        else if (per > 50) System.out.println("Your grade: D");
        else System.out.println("Your grade: F");
    }
}