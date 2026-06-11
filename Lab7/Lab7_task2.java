import java.util.Scanner;
class Pupil {
    private String studentName;
    private int rollNumber;

    static Scanner sc = new Scanner(System.in);

    Pupil(String studentName, int rollNumber) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        String userChoice;

        System.out.println(this.buildInfo());

        System.out.print("Do You want to Change your Name (yes / no): ");
        userChoice = sc.next();

        if (userChoice.equalsIgnoreCase("yes")) updateName();
    }

    int fetchRoll() {
        return rollNumber;
    }

    String fetchName() {
        return studentName;
    }

    String showInfo() {
        return buildInfo();
    }

    private String buildInfo() {
        return "Name: " + studentName + ". Roll_No: " + rollNumber;
    }

    void updateName(String studentName) {
        this.studentName = studentName;
    }

    void updateName() {
        System.out.print("Enter new Name: ");
        sc.nextLine();
        this.studentName = sc.nextLine();
    }

//    @Override
//    public String toString() {
//        return "Name: " + studentName + ". Roll_No: " + rollNumber;
//    }
}
public class Lab7_task2 {
    public static void main(String[] args) {
        Pupil pupil1 = new Pupil("Ali", 134);
//      System.out.println(pupil1);

//      pupil1.updateName("Hassan");
        System.out.println(pupil1.showInfo());
//      System.out.println(pupil1.fetchName());
//      System.out.println(pupil1.fetchRoll());
    }
}