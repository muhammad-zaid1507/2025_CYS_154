abstract class Bank {
    abstract void processPayment(int amount);
}

class Mashreq extends Bank {
    int balancePaid;

    @Override
    void processPayment(int amount) {
        System.out.println("Processing Mashreq payment of: Rs. " + amount);
        this.balancePaid = amount;
    }
}

class Jazzcash extends Bank {
    int balancePaid;

    @Override
    void processPayment(int amount) {
        // Added a print statement here so it doesn't look half-finished!
        System.out.println("Processing Jazzcash payment of: Rs. " + amount);
        this.balancePaid = amount;
    }
}

public class Lab8_task2 {
    public static void main(String[] args) {
        Mashreq mashreqApp = new Mashreq();
        mashreqApp.processPayment(900);

        // I added this quick test for Jazzcash to show both classes work
        Jazzcash jazzApp = new Jazzcash();
        jazzApp.processPayment(450);
    }
}