public class Lab2_Task7 {
    public static void main(String[] args) {
        int Hour = 14;

        if (Hour >= 5 && Hour <= 11)
        {
            System.out.println("Good Morning");
        } else if (Hour >= 12 && Hour <= 17) {
            System.out.println("Good Afternoon");
        } else if (Hour >= 18 && Hour <= 23) {
            System.out.println("Good Evening");
        } else {
            System.out.println("Invalid Hour");
        }
    }
}
