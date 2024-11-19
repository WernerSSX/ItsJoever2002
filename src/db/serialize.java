package db;

public interface serialize<T> {
    public String serialize(T data);
    public T deserialize(String data);
}
