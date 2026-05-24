package OOP_LAB_3;

public class LAB_Task_1_LAB_3 {
    static void main(String[] args) {
        ezpaisa cash = new ezpaisa();
        jazzcash money = new jazzcash();
        UBL ammount = new UBL();
        cash.pay(200);
        money.pay(500);
        ammount.pay(50000);
    }

}


abstract class bank_account{
    int cash;
    abstract void pay(int cash);
}



class ezpaisa extends bank_account{
    @Override
    void pay(int c){
        System.out.println("In Ezpaisa Account you have: " + c);
    }
}

class jazzcash extends bank_account{
    @Override
    void pay(int c){
        System.out.println("In JazzCash Account you have: " + c);
    }
}

class UBL extends bank_account {
    @Override
    void pay(int c){
        System.out.println("In UBL Account you have: " + c);
    }
}