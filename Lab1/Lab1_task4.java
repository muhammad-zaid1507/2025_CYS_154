import java.util.Scanner;

public class Lab1_task4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Units Consumed: ");
        int units = sc.nextInt();
        int bill = 0;

        if (units <= 100) {
            bill = units * 5;
        } else if (units <= 200) {
            bill = (100 * 5) + ((units - 100) * 7);
        } else {
            bill = (100 * 5) + (100 * 7) + ((units - 200) * 15);
        }

        System.out.println("Bill: " + bill);
    }
}

