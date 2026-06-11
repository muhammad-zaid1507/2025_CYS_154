abstract class Creatures {
    abstract void displayInfo();
}

class Tiger extends Creatures {
    String creatureName;
    String habitat;

    Tiger(String creatureName, String habitat) {
        this.creatureName = creatureName;
        this.habitat = habitat;
    }

    @Override
    void displayInfo() {
        System.out.println("Name: " + creatureName);
        System.out.println("Habitat: " + habitat);
    }
}

class Lab10_task1 {
    public static void main(String[] args) {
        Tiger myAnimal = new Tiger("Tiger", "Wild");
        myAnimal.displayInfo();
    }
}