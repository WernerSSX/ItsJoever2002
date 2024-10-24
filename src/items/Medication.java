package items;

/**
 * The Medication class represents a medication in the inventory.
 */
public class Medication {
    private String name;
    private int quantity;
    private String supplier;

    public Medication(String name, int quantity, String supplier) {
        this.name = name;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	
	public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
