package OOP_LAB_2;

public class TASK_1_LAB2 {
    static void main(String[] args) {
        ss s1 = new ss("Zaid" , 154);
        s1.display();
    }
}


class ss{
    private String Name;
    private int roll;
    public ss(String Name , int roll){
        this.Name = Name;
        this.roll = roll;
    }
    public void display(){
        System.out.println("Name: " + Name + " | Roll No: " + roll);
    }
}