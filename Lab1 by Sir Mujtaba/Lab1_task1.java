import java.util.Scanner;

public class Lab1_task1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number?");
        int n = input.nextInt();

        if (n < 0) {
            System.out.println("Factorial of neg num is undefined.");
        } else {
            long fact = 1;
            for (int i = 1; i <= n; i++) {
                fact *= i;
            }
            System.out.println("Factorial: " + fact);
        }
    }
}