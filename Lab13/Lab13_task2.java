public class Lab13_task2 {
    public static void main(String[] args) {
        Puppy puppy = new Puppy("Puppy");
        puppy.makeSound();
        puppy.display();
    }
}

class Creaturer {
    String name;

    public void makeSound() {
        System.out.println("Animal makes sound");
    }
}

class Puppy extends Creaturer {
    String name;

    public Puppy(String name) {
        super();
        this.name = name;
    }

    public void display() {
        System.out.println("Dog is an Animal");
    }
}