public class Lab6_task3 {
    public static void main(String[] args) {
        Cow myCow = new Cow();
        myCow.eat();
        myCow.moo();
        myCow.graze();
    }
}

class Creature {
    public void eat() {
        System.out.println("Creature is Eating");
    }
}

class Cow extends Creature {
    public void moo() {
        System.out.println("Moo Moo");
    }

    public void graze() {
        System.out.println("Gaaye Ghas Kha Rahi Hai");
    }
}