public class Lab11_task3 {
    public static void main(String[] args) {
        Member m = new Member();
        m.showName();
        m.showRoll();
        m.showReg();
    }
}

interface Pupilz {
    void showName();
    void showRoll();
}

interface Faculty {
    void showReg();
}

class Member implements Pupilz, Faculty {
    @Override
    public void showReg() {
        System.out.println("My reg. is 2025-CYS-154");
    }

    @Override
    public void showName() {
        System.out.println("My name is Zaid");
    }

    @Override
    public void showRoll() {
        System.out.println("My roll number is 154");
    }
}