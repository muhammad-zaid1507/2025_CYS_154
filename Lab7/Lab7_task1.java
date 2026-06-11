class Learner {
    private String studentName;
    private int rollNumber;

    Learner(String studentName, int rollNumber) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;

        System.out.println(this.buildInfo());
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

//    @Override
//    public String toString() {
//        return "Name: " + studentName + ". Roll_No: " + rollNumber;
//    }
}

public class Lab7_task1 {
    public static void main(String[] args) {
        Learner learner1 = new Learner("Zainuuu", 194);

        System.out.println(learner1.showInfo());
//      System.out.println(learner1);
//      System.out.println(learner1.fetchName());
//      System.out.println(learner1.fetchRoll());
    }
}