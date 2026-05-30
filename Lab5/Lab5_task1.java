class St_Rec {
    private String Name;
    private int roll_no;

    public St_Rec(String Name, int roll_no) {
        this.Name = Name;
        this.roll_no = roll_no;
    }

    public String getName() {
        return Name;
    }

    public void setName(String fullName) {
        this.Name = fullName;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(int studentId) {
        this.roll_no = roll_no;
    }
}

public class Lab5_task1 {
    public static void main(String[] args) {
        St_Rec st = new St_Rec("Zaid", 174);
        System.out.println("Name: " + st.getName() + " AND ID: " + st.getRoll_no());

        st.setName("MANGO");
        st.setRoll_no(154);
        System.out.println("Name: " + st.getName() + " AND ID: " + st.getRoll_no());
    }
}
