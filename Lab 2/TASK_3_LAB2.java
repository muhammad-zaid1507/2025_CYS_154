package OOP_LAB_2;

import java.io.*;
import java.util.Scanner;

public class TASK_3_LAB2 {
    static void main(String[] args) throws IOException {
        File f1 = new File("Data.txt");
        f1.createNewFile();
        FileWriter file = new FileWriter(f1 , true );
        file.write("Name: Zaid Khalid\n");
        file.write("Roll No: 154\n");
        file.close();
    }
}