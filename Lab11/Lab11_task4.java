public class Lab11_task4 {
    public static void main(String[] args) {
        Batchmate b = new Participant();
        b.showRegistration();
    }
}

abstract class Scholar {
    abstract void showName();

    public void showRoll() {
        System.out.println("My roll number is 154");
    }

    void showSection() {
        System.out.println("My section is C");
    }
}

abstract class Batchmate {
    abstract void showRegistration();
}

class Participant extends Batchmate {
    @Override
    public void showRegistration() {
        System.out.println("My Reg. is 2025-CYS-154");
    }
}