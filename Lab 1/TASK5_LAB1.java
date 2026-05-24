package OOP_LAB_1;
import java.util.*;
public class TASK5_LAB1 {

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        percetages p = new percetages();
        System.out.println("Enter total marks: ");
        double a = x.nextDouble();
        System.out.println("Enter obtained marks: ");
        double b = x.nextDouble();
        p.setObtained(a);
        p.setTotal(b);
        p.res(a , b);

    }
}

class percetages{
    private double total , obtained;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getObtained() {
        return obtained;
    }

    public void setObtained(double obtained) {
        this.obtained = obtained;
    }
    public void res (double t , double o){
        double ress = (o * 100) / t;
        System.out.println("Your percentage is: " + ress);
    }
}