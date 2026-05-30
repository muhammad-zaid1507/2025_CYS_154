import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Lab7_task1 {

    private static final String FILE_PATH = "Getter.txt";

    public static void main(String[] args) throws IOException {
        writeToFile(FILE_PATH, "Hello World..\n");
        readFromFile(FILE_PATH);
    }
    public static void readFromFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            System.out.println(line);
        }
    }
    public static void createFile() throws IOException {
        File myFile = new File(FILE_PATH);
        if (myFile.createNewFile()) {
            System.out.println("File created");
        } else {
            System.out.println("File Exists");
        }
    }
    public static void writeToFile(String fileName, String message) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(message);
        }
    }
}