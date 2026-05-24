package OOP_LAB_2;
import java.io.*;
import java.util.Scanner;

public class TASK_4_LAB2 {
    static void main(String[] args) throws IOException {
        File f1 = new File("daata_base.txt");
        Scanner read = new Scanner(f1);
        while(read.hasNextLine()){
            System.out.println(read.nextLine());
        }
    }
}

