public class Lab13_task1 {
    public static void main(String[] args) {
        Pet pet = new Pet("pet1");
        Caretaker caretaker = new Caretaker("Ahmed", 18);
        caretaker.assignPet(pet);
        caretaker.makeSound();
        pet.display();
    }
}

class Caretaker {
    Pet pet;
    String name;
    int age;

    public Caretaker(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void assignPet(Pet pet) {
        this.pet = pet;
    }

    public void makeSound() {
        System.out.println("Animal makes sound");
        if (pet != null) {
            System.out.println(pet.petName);
        }
    }
}

class Pet {
    Caretaker caretaker1 = new Caretaker("Zaid", 18);
    String petName;

    public Pet(String petName) {
        this.petName = petName;
    }

    public void display() {
        System.out.println("Bark");
        System.out.println(caretaker1.name);
        System.out.println(caretaker1.age);
    }
}