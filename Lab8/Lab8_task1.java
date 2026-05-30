abstract class Animal {
    abstract void displayInfo();
}

class Lion extends Animal {
    String name;
    String category;

    Lion(String name, String category) {
        this.name = name;
        this.category = category;
    }

    @Override
    void displayInfo() {
        System.out.println("Animal Name: " + name);
        System.out.println("Category: " + category);
    }
}

public class Lab8_task1 {
    public static void main(String[] args) {
        // Renamed from Stu to match your file name
        Lion myLion = new Lion("Lion", "Wild");
        myLion.displayInfo();
    }
}