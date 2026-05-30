public class Lab6_task1 {
    static void main(String[] args) {
        students st = new students("Zaid", 154);

        System.out.println(st.Display());
//      System.out.println(st);
//      System.out.println(st.get_Name());
//      System.out.println(st.get_Roll_no());
    }
}


class students{
    private String Name;
    private int Roll_no;

    students(String Name, int Roll_no){
        this.Name = Name;
        this.Roll_no = Roll_no;

        System.out.println(this.Student_Display());
        //System.out.println("Name: " + Name + " Roll_No: " + Roll_no);
    }

    int get_Roll_no(){
        return Roll_no;
    }

    String get_Name(){
        return Name;
    }

    String Display(){
        return Student_Display();
    }

    private String Student_Display(){
        return "Name: " + Name + ". Roll_No: " + Roll_no;
    }


//    @Override
//    public String toString(){
//        return "Name: " + Name + ". Roll_No: " + Roll_no;
//    }

}