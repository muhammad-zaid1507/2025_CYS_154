package OOP_LAB_1;

import java.util.*;
public class TASK2_LAB1 {

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.println("Enter total marks: ");
        double a = x.nextDouble();
        System.out.println("Enter obtained marks: ");
        double b = x.nextDouble();
        pe p =  new pe(a , b);
        System.out.println("Your percentage is: " + p.getRes());
        p.grade();
    }
}

class pe{
    private double total , obtained , res;

    public pe (double t , double o){
        this.obtained = o;
        this.total = t;
        res = (o * 100) / t;
    }
    public double getRes(){
        return res;
    }
    void grade(){
        if(res >= 90){
            System.out.println("Your grade is: A");
        }
        else if(res >= 85 ){
            System.out.println("Your grade is: A-");
        }
        else if(res >= 80 ){
            System.out.println("Your grade is: B+");
        }
        else if(res >= 75 ){
            System.out.println("Your grade is: B");
        }
        else if(res >= 70 ){
            System.out.println("Your grade is: B-");
        }
        else if(res > 65 ){
            System.out.println("Your grade is: C+");
        }
        else if(res > 60 ){
            System.out.println("Your grade is: C");
        }
        else if(res > 55 ){
            System.out.println("Your grade is: C-");
        }
        else if(res > 50 ){
            System.out.println("Your grade is: D");
        }
        else{
            System.out.println("Your grade is: F");
        }
    }

}

