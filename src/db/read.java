package db;

import java.io.IOException;
import java.util.List;

public interface read {
    public List<String> read(String fileName) throws IOException;
}
