import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lab8_task1 {

    public static void main(String[] args) throws IOException {
        File myFile = new File("Records.txt");

//        createFile(myFile);
//        myFile = renameFile(myFile, "Records.csv");
//        System.out.println(myFile.getName());
        writeToFile(myFile, "Name: Hassan\nRollNo: 166");
        readFromFile(myFile);
    }

    static void createFile(File myFile) throws IOException {
        if (!myFile.exists()) {
            myFile.createNewFile();
            System.out.println(myFile.exists());
        } else {
            System.out.println("File Already Exists");
        }
    }

    static File renameFile(File myFile, String newName) {
        myFile = null;
        myFile = new File("./" + newName);
        return myFile;
    }

    static void writeToFile(File myFile, String content) throws IOException {
        PrintWriter pw = new PrintWriter(myFile);
        pw.println(content);
        pw.close();
    }

    static void readFromFile(File myFile) throws IOException {
        System.out.println(Files.readString(Path.of(myFile.getPath())));
    }
}