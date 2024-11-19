package db;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DataLoader
 * Abstract base class for loading and saving data from files.
 * 
 * This class provides basic methods for reading and writing string data to files.
 * Subclasses can specify the type of data (generic type T) they load and save, 
 * implementing specific deserialization and serialization methods.
 *
 * T Type of object managed by the DataLoader.
 */
public abstract class DataLoader<T> {  // Generic type T to represent the type of object we are loading
    protected String filePath; /**< Path to the file from which data is loaded and saved. */
    public static final String SEPARATOR = "|"; /**< Separator used in serialized data representation. */

    /**
     * Constructs a DataLoader with the specified file path.
     *
     * @param filePath Path to the file associated with this DataLoader.
     */
    public DataLoader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads all lines from a specified file.
     *
     * @param fileName Name of the file to read data from.
     * @return List of strings, each representing a line from the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
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

    /**
     * Writes a list of strings to a specified file.
     *
     * @param fileName Name of the file to write data to.
     * @param data List of strings to be written to the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
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

    /**
     * Abstract method to load data from the associated file.
     *
     * Subclasses must implement this method to load their specific type of data.
     *
     * @throws IOException If an error occurs during file reading.
     */
    public abstract void loadData() throws IOException;

    /**
     * Abstract method to save data to the associated file.
     *
     * Subclasses must implement this method to save their specific type of data.
     *
     * @throws IOException If an error occurs during file writing.
     */
    public abstract void saveData() throws IOException;

    /**
     * Abstract method to deserialize a string into an object of type T.
     *
     * Subclasses must implement this method to convert serialized data into 
     * their specific data type.
     *
     * @param data String data to deserialize.
     * @return Deserialized object of type T.
     */
    protected abstract T deserialize(String data);

    /**
     * Abstract method to serialize an object of type T into a string.
     *
     * Subclasses must implement this method to convert their specific data 
     * type into a serialized string representation.
     *
     * @param data Object of type T to serialize.
     * @return Serialized string representation of the object.
     */
    protected abstract String serialize(T data);
}
