import java.util.Scanner;

public class Lab1_task2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Starting value? ");
        int start = input.nextInt();
        System.out.print("Ending value? ");
        int end = input.nextInt();

        if (start <= end) {
            for (int i = start; i <= end; i++) {
                System.out.print(i + " ");
            }
        } else {
            for (int i = start; i >= end; i--) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}