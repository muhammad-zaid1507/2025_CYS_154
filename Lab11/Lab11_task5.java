public class Lab11_task5 {
    public static void main(String[] args) {
        Candidate c = new Candidate();
        c.showName();
        c.showRoll();
        c.showReg();
    }
}

interface Peer {
    void showName();
    void showRoll();
}

abstract class Instructor {
    abstract void showReg();
}

class Candidate extends Instructor implements Peer {
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