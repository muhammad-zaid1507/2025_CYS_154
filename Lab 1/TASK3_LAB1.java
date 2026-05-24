import java.io.*;
import java.util.*;
public class TASK3_LAB1 {
    static String a;
    public static void main(String[] args) throws IOException {
        Scanner x = new Scanner(System.in);
        perr pg = new perr();
        System.out.println("Do you want to access results in the file? | Yes or No");
        String y = x.nextLine();
        if(y.equalsIgnoreCase("yes")){
            System.out.println("Enter grades(A+ , A , B , etc): ");
            String gr = x.nextLine();
            pg.Access_Grades(gr);
        }
        else{
            System.exit(0);
        }
        System.out.println("\nDo you want to add another result? | Yes or No");
        String add = x.nextLine();
        if(add.equalsIgnoreCase("Yes")) {
            System.out.println("Enter total marks: ");
            double a = x.nextDouble();
            System.out.println("Enter obtained marks: ");
            double b = x.nextDouble();
            perr p = new perr(a, b);
            System.out.printf("Your percentage is: %.2f\n", p.getRes());
            String g = p.grade();
            System.out.println("Your grade is: " + g);
            p.addGrades(p);
        }
        else {
            System.exit(0);
        }
    }
}

class perr{
    private double total , obtained , res;
    public perr(){
    }
    public perr(double t , double o){
        this.obtained = o;
        this.total = t;
        res = (o * 100) / t;
        res = Math.round(res * 100.0) / 100.0;
    }
    public Double getRes(){
        return res;
    }
    String grade(){
        if(res >= 90){
            return "A";
        }
        else if(res >= 85 ){
            return "A-";
        }
        else if(res >= 80 ){
            return "B+";
        }
        else if(res >= 75 ){
            return "B";
        }
        else if(res >= 70 ){
            return "B-";
        }
        else if(res > 65 ){
            return "C+";
        }
        else if(res > 60 ){
            return "C";
        }
        else if(res > 55 ){
            return "C-";
        }
        else if(res > 50 ){
            return "D";
        }
        else{
            return "F";
        }
    }

    // Creating and Writing in the file

    public void addGrades(perr p) throws IOException{

        FileWriter f = new FileWriter("Grades.txt" , true);
        String g = p.grade();
        f.write("Result: " + String.format("%.2f" , p.res) + "% | Grade: " + g + "\n");
        f.close();
    }


    public void Access_Grades(String grade) throws IOException{
            File f = new File("Grades.txt");
            Scanner r = new Scanner(f);

            System.out.println("\n------- Filtered Results -------");

            while(r.hasNextLine()){
                String l = r.nextLine();
                String[] parts = l.split("\\|");
                String grade_Part = parts[1].trim();
                String actual_grade = grade_Part.replace("Grade: " , "");

                if(actual_grade.equalsIgnoreCase(grade)){
                    System.out.println(l);
                }
            }
            r.close();
        }
}