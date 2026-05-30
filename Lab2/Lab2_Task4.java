public class Lab2_Task4 {
    public static void main(String[] args) {
        boolean hasID = true;
        boolean isOver18 = true;

        if (hasID && isOver18) {
            System.out.println("Access Granted");
        }

        if (hasID || isOver18) {
            System.out.println("Special Guest");
        }
    }
}