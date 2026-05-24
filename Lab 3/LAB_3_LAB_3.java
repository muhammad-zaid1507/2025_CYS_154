package OOP_LAB_3;

public class LAB_3_LAB_3 {
    static void main(String[] args) {
     Elephant e1 = new Elephant();
     e1.display();
     Cat c1 = new Cat();
     c1.display();
    }
}


abstract class Animal{
    String type;
    abstract void display();
}


class Elephant extends Animal{
    void display(){
        System.out.println("I am an Elephant");
    }
}

class Cat extends Animal{
    void display(){
        System.out.println("I am a cat");
    }
}


