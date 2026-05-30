import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lab6_task3 {

     public static void main(String[] args) throws IOException {
        File file = new File("Data.txt");

        // Files files = new Files();


        create(file);
        file = rename(file, "Data.csv");
        System.out.println(file.getName());
        write(file, "Name: Waleed\nRollNo: 149");
        read(file);


    }

    static void create(File file) throws IOException{
        if(!file.exists()) {
            file.createNewFile();
            System.out.println(file.exists());
        }
        else {
            System.out.println("File Exists");
        }
    }

    static File rename(File file, String Name){
        file = null;
        file = new File("C:\\Users\\hp\\OneDrive\\Desktop\\Jav\\Java\\src\\Data.txt");
        return file;
    }

    static void write(File file, String Message) throws IOException {
        PrintWriter writer = new PrintWriter(file);
        writer.println(Message);
        writer.close();
    }

    static void read(File file) throws IOException {
        System.out.println(Files.readString(Path.of(file.getPath())));
    }

}