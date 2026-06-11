import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Lab8_task2 {

    static String filePath = "Output.txt";
    static File targetFile = new File("Output.txt");

    public static void main(String[] args) throws IOException {
//        createFile();
        appendToFile(filePath, "Hello World..\n");
        displayLines(filePath);
    }

    static void displayLines(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            System.out.println(line);
        }
    }

    static void createFile() throws IOException {
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        } else {
            System.out.println("File Already Exists");
        }
    }

    static void appendToFile(String fileName, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        fileWriter.write(content);
        fileWriter.close();
    }
}