public class Lab11_task1 {
    public static void main(String[] args) {
        Myself me = new Myself();
        me.showName();
        me.showRoll();
    }

    interface Learner {
        void showName();
        void showRoll();
    }

    static class Myself implements Learner {
        @Override
        public void showName() {
            System.out.println("My name is Zaid");
        }

        @Override
        public void showRoll() {
            System.out.println("My roll no is 154");
        }
    }
}