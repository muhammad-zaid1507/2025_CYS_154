import java.util.*;

// Main Class

public class Lab4_task2 {

    public static void main(String[] args) {
        int choice, accn, ad;
        String hname;
        float bal, amt;
        boolean running = true;
        accounts found = null;
        Scanner x = new Scanner(System.in);
        ArrayList<accounts> Account = new ArrayList<>();

        // Bank Menu

        System.out.println("\n       ------- BANK MENU ------- ");
        System.out.println("Type the number for required process  (Creating account first is compulsory)\n");
        System.out.println("1.Create Account");
        System.out.println("2.Deposit Money (Not more than 50000 at a time)");
        System.out.println("3.Withdraw Money (Not more than 20000 at a time)");
        System.out.println("4.Display Account");
        System.out.println("5.Exit");

        // Infinite Loop

        while (running) {
            choice = x.nextInt();
            switch (choice) {

                // Create account

                case 1:
                    x.nextLine();
                    System.out.println("Enter Account Holder name: ");
                    hname = x.nextLine();
                    System.out.println("Enter Account Number: ");
                    accn = x.nextInt();
                    System.out.println("Enter Initial Balance: ");
                    bal = x.nextFloat();
                    Account.add(new accounts(hname, accn, bal));
                    System.out.println("Account created successfully! ");
                    break;

                // Deposit Money

                case 2:
                    System.out.println("Enter Account Number: ");
                    ad = x.nextInt();
                    found = accounts.find_account(Account, ad);
                    if (found != null) {
                        System.out.println("Enter amount to deposit: ");
                        amt = x.nextFloat();
                        found.Deposit(amt);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                // Withdraw Money

                case 3:
                    System.out.println("Enter Acccount Number: ");
                    ad = x.nextInt();
                    found = accounts.find_account(Account, ad);
                    if (found != null) {
                        System.out.println("Enter the amount of withdrawl: ");
                        amt = x.nextFloat();
                        found.Withdrawl(amt);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                // Display Account

                case 4:
                    System.out.println("Enter the Account Number: ");
                    ad = x.nextInt();
                    found = accounts.find_account(Account, ad);
                    if (found != null) {
                        found.display();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
    }

}

// Actual class for working of a Bank Management System

class accounts{
    private String holder_name;
    private int account_no;
    private float balance;
    accounts(String holder_name , int account_no , float balance){
        this.holder_name = holder_name;
        this.account_no = account_no;
        this.balance = balance;
    }

    public String getName() {
        return holder_name;
    }

    public int getAccount_no() {
        return account_no;
    }

    public float getBalance() {
        return balance;
    }

    // Method for Depositing money into the selected account

    void Deposit(float amount)
    {
        if(amount > 0f && amount <= 50000f)  
        {
            balance += amount;
            System.out.println("After Depositing " + amount +  " the total balance is: " + balance);

        }
        else
        {
            System.out.println("Invalid Amount!!!");
        }
        System.out.println("\n ------------------------------");
    }

    // Method for Withdrawing money from the selected account

    void Withdrawl(float amount)
    {
        if(amount > 0f && amount <= balance && amount <= 20000f)
        {
            balance -= amount;
            System.out.println("After Withdrawl of " + amount + " Total balance is : " + balance);
        }
        else
        {
            System.out.println("Invalid Amount!!!");
        }
        System.out.println("\n ------------------------------");
    }

    // Method for Displaying the Account details

    public void display()
    {
        System.out.println("\n Holder Name: " + holder_name);
        System.out.println("\n Account Number: " + account_no);
        System.out.println("\n Current Balance: " + balance);
        System.out.println("\n ------------------------------");
    }

    // Finding the Account if it exist or not in the Arraylist

    public static accounts find_account(ArrayList<accounts> Accounts , int Acc_no){
        for(accounts acc : Accounts){
            if(acc.getAccount_no() == Acc_no){
                return acc;
            }
        }
        return null;
    }
}
