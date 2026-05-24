package OOP_LAB_1;

public class TASK4_LAB1 {
    static void main(String[] args) {
        bakri bakri1 = new bakri();
        bakri1.eat();
        bakri1.bark();
        bakri1.un();

    }

}


class Animal{
    public void eat(){
        System.out.println("Bakri khana thoos rahi ha");
    }

}

class dog extends Animal{
    public void bark() {
        System.out.println("Ab wo bhau bhau kar rahi ha ");
    }
}

class bakri extends dog{

    public void un() {
        System.out.println("Bakri nukhri ha! ");
    }
}