import java.util.Scanner;

public class Lab3_Task3 {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Assets a = new Assets();
        System.out.println("Enter Total Cost of your asset: ");
        long t_cost = input.nextLong();
        a.total_cost(t_cost);
        System.out.println("Enter your salary: ");
        float t_salary = input.nextFloat();
        System.out.println("Enter the percentage of your salary you want to to save: ");
        int p_saved = input.nextInt();
        System.out.println(a.Total_months(t_salary , p_saved));
    }
}

class Assets{
    // Method for calculating Down Payment

    float down_payment;

    public float total_cost(long Total_cost){
        down_payment = Total_cost * 0.25f; // 25% of Total Asset's cost
        return down_payment;
    }

    // Method for Calculating No. of months to achieve Down Payment

    float current_savings = 0f , annual_return = 0.04f;
    int months = 0;

    public int Total_months(float salary , int Portion_saved){
        while(current_savings <= down_payment){

            // Adding annual return of 0.04(4%)
            current_savings += current_savings * (annual_return / 12);

            // Adding portion of percentage saved from salary
            current_savings += salary * ((Portion_saved / 100f));

            // Adding months accordingly
            months++;
        }
        return months;
    }
}
