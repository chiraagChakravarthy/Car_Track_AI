package car_track.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    public static void writeToFile(String filePath, ArrayList<String> text){
        Path path = Paths.get(new File(filePath).toURI());
        try {
            Files.write(path, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFromFile(String filePath){
        File file = new File(filePath);
        ArrayList<String> data = new ArrayList<>();
        try {
            Scanner input = new Scanner(new FileInputStream(file));
            while (input.hasNextLine()){
                data.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            //not swallowed. Will result in an empty arraylist. This would never happen otherwise.
        }
        return data;
    }
}