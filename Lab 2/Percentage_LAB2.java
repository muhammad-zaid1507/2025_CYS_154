package OOP_LAB_2;

import java.util.*;

public class Percentage_LAB2 {
    static List<Double> percentage = new ArrayList<>();
    static void perc(ArrayList<Integer> marks){
        for(int m : marks){
            percentage.add((m/50.0) * 100);
        }
    }
    static void display_Percentage(){
        for(int i = 0; i < TASK_5_LAB2.names.size(); i++){
            System.out.println("Name: " + TASK_5_LAB2.names.get(i) + "\nRoll No. " + TASK_5_LAB2.Roll.get(i)+ "\nPercentage: " + percentage.get(i) + "%" + "\nGrade: " + Grade(percentage.get(i)));
            System.out.println();
        }
    }

    static String Grade(double per){
        if(!(per > 100)){
            if (per >= 90){
                return "A";
            }
            else if(per >= 80){
                return "B";
            }
            else if(per >= 70){
                return "C";
            }
            else if(per >= 60){
                return "D";
            }
            else{
                return "F";
            }
        }
        else{
            System.out.println("Fail");
        }
        return "Invalid Grade";
    }
}


