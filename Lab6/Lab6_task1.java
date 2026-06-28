class StudentInfo {
    private String studentName;
    private int enrollmentId;

    public StudentInfo(String studentName, int enrollmentId) {
        this.studentName = studentName;
        this.enrollmentId = enrollmentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}

public class Lab6_task1 {
    public static void main(String[] args) {
        StudentInfo record = new StudentInfo("omarrrrr", 194);
        System.out.println("Name: " + record.getStudentName() + " | ID: " + record.getEnrollmentId());

        record.setStudentName("Ahmad");
        record.setEnrollmentId(155);
        System.out.println("Name: " + record.getStudentName() + " | ID: " + record.getEnrollmentId());
    }
}