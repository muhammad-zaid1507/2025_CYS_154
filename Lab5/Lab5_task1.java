public class Lab5_task1 {
    public static void main(String[] args) {
        StudentS student1 = new StudentS("Zaid", 154);
        System.out.println(student1.fullName);
    }
}

class StudentS {
    String fullName;
    private int rollNumber;

    StudentS(String fullName, int rollNumber) {
        this.fullName = fullName;
        this.rollNumber = rollNumber;
    }
}