package OOP_LAB_1;

class Person{
    private String name;
    private int id;

// Constructor

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

// Getter

    public String getName() {
        return name;
    }

// Setter

    public void setName(String name) {
        this.name = name;
    }

// Getter

    public int getId() {
        return id;
    }

// Setter

    public void setId(int id) {
        this.id = id;
    }
}
public class TASK1_LAB1 {
    public static void main(String[] args) {
        Person a = new Person("Zaid" , 154 );
        System.out.println("Name is: " + a.getName() + " \t|\twith ID: " + a.getId());

        // After setting new values

        a.setName("Waleed Siyal");
        a.setId(149);
        System.out.println("Name is: " + a.getName() + " \t|\twith ID: " + a.getId());
    }
}
