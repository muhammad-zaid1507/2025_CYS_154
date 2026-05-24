package OOP_LAB_1;
import java.util.*;
public class TASK6_LAB1 {

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.println("Enter total marks: ");
        double a = x.nextDouble();
        System.out.println("Enter obtained marks: ");
        double b = x.nextDouble();
        percent p =  new percent(a , b);
        System.out.println("Your percentage is: " + p.getRes());
    }
}

class percent{
    private double total , obtained , res;

    public percent (double t , double o){
        this.obtained = o;
        this.total = t;
        this.res = res;
        res = (o * 100) / t;
    }
    public double getRes(){
        return res;
    }

}
