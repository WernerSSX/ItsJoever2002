package db;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class DataLoader<T> {  // Generic type T to represent the type of object we are loading
    protected String filePath;
    public static final String SEPARATOR = "|";

    public DataLoader(String filePath) {
        this.filePath = filePath;
    }

    // Reading the file
    public static List<String> read(String fileName) throws IOException {
        List<String> data = new ArrayList<>();
        FileInputStream fis = null;
        Scanner scanner = null;
        try {
            fis = new FileInputStream(fileName);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return data;
    }

    // Writing data to file
    public static void write(String fileName, List<String> data) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        try {
            for (String line : data) {
                out.println(line);
            }
        } finally {
            out.close();
        }
    }

    public abstract void loadData() throws IOException;
    public abstract void saveData() throws IOException;
    protected abstract T deserialize(String data);
    protected abstract String serialize(T data);
}
