abstract class PaymentService {
    abstract void makePayment(int amount);
}

class EasyPaisa extends PaymentService {
    int totalAmount;

    @Override
    void makePayment(int amount) {
        System.out.println("You are going to pay: " + amount);
        this.totalAmount = amount;
    }
}

class SadaPay extends PaymentService {
    int totalAmount;

    @Override
    void makePayment(int amount) {
        // payment logic
    }
}

class Lab10_task2 {
    public static void main(String[] args) {
        EasyPaisa app = new EasyPaisa();
        app.makePayment(1300);
    }
}