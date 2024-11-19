package db;

import java.io.IOException;
import java.util.List;

public interface write {
    public void write(String fileName, List<String> data) throws IOException;
}
