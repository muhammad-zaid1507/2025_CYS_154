import java.util.Scanner;

public class Lab1_task5 {
    public static void main(String[] args){
        int x , count =1 , div;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a Positive Integer: ");
        x= input.nextInt();
        System.out.println("input: " + x);

        if(x>0)
        {
            while (x>1)
            {
                x=x/2;
                count++;
            }
        }
        else
        {
            System.out.println("Invalid entry");
        }
        System.out.println("\n Div " + count);
        input.close();
    }
}
