package OOP_LAB_2;

public class TASK_2_LAB2 {
    static void main(String[] args) {
        s s1 = new s("Zaid" , 154);
        s1.setName("ali"); s1.setRoll(156);
        s1.info();

    }

}

class s{
    private String name;
    private int roll;
    public s(String name, int roll){
        this.name = name;
        this.roll = roll;
        System.out.println("Name: " + getName() + " | Roll No: " + getRoll());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void info(){
        System.out.println("Name: " + getName() + " | Roll No: " + getRoll());
    }
}