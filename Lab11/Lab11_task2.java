public class Lab11_task2 {
    public static void main(String[] args) {
        Enrolled me = new Myself();
        me.showName();
        me.showRoll();
    }
}

abstract class Enrolled {
    abstract void showName();
    abstract void showRoll();
    void showSection() {
        System.out.println("My section is C");
    }
}

class Myself extends Enrolled {
    @Override
    public void showName() {
        System.out.println("My name is Zaid");
    }

    @Override
    public void showRoll() {
        System.out.println("My roll no is 154");
    }
}