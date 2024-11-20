package db.db_interface;

public interface serialize<T> {
    public String serialize(T data);
    public T deserialize(String data);
}
