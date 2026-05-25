import java.util.Scanner;

public class Lab1_task6 {
    public static void main(String[] args){
        int value , a , reverse=0;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter value? ");
        value=input.nextInt();
        System.out.println("Number: " + value);
        while(value>0){
            a=value%10;
            reverse=reverse*10 + a;
            value=value/10;
        }
        System.out.println("\n inverse of value is: "+ reverse);
        input.close();
    }
}
