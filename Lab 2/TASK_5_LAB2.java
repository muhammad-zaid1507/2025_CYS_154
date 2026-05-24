package OOP_LAB_2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TASK_5_LAB2 {

    static ArrayList<Integer> marks = new ArrayList<>();
    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<Integer> Roll = new ArrayList<>();

    static File f = new File("Data.txt");


    public static void main(String[] args) throws IOException {
        read_marks();
        read_names();
        read_roll();
        Percentage_LAB2.perc(marks);
        Percentage_LAB2.display_Percentage();
    }
    public static void read_names() throws IOException{
        Scanner s = new Scanner(f);
        while(s.hasNextLine()){
            String name = s.nextLine().split(",")[0];
            names.add(name);
        }
    }
    public static void read_marks() throws IOException {
        Scanner s = new Scanner(f);
        while(s.hasNextLine()){
            String Marks = s.nextLine().split(",")[1];
            marks.add(Integer.parseInt(Marks));
        }
    }
    public static void read_roll() throws IOException{
        Scanner s = new Scanner(f);
        while(s.hasNextLine()){
            String roll = s.nextLine().split(",")[2];
            Roll.add(Integer.parseInt(roll));
        }
    }













//    static int Average(){
//        int sum = 0;
//        int avg = 0;
//        for(int a : marks){
//             sum = (sum + a) ;
//        }
//        avg = sum /3;
//        System.out.println(avg);
//        return avg;
//
//    }
}
