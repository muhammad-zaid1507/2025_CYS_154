public class Lab5_task3 {
    public static void main(String[] args) {
        Goat bakra = new Goat();
        bakra.eat();
        bakra.bleat();
        bakra.graze();
    }
}

class animal {
    public void eat() {
        System.out.println("janwar is Eating");
    }
}

class Goat extends animal {
    public void bleat() {
        System.out.println("meow bhau");
    }

    public void graze() {
        System.out.println("Bakra thus rha hei");
    }
}