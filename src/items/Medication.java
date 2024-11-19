package items;

/**
 * Medication
 * Represents a medication in the inventory, with details about its name, quantity, and supplier.
 */
public class Medication {
    private String name; /**< Name of the medication */
    private int quantity; /**< Quantity of the medication in stock */
    private String supplier; /**< Supplier of the medication */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs a Medication with specified details.
     *
     * @param name     The name of the medication.
     * @param quantity The quantity available in stock.
     * @param supplier The supplier of the medication.
     */
    public Medication(String name, int quantity, String supplier) {
        this.name = name;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the name of the medication.
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medication.
     * @param name The new name of the medication.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the quantity of medication in stock.
     * @return The quantity of medication in stock.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of medication in stock.
     * @param quantity The new quantity of medication in stock.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the supplier of the medication.
     * @return The supplier of the medication.
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Sets the supplier of the medication.
     * @param supplier The new supplier of the medication.
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**********
     * Methods *
     **********/

    /**
     * Returns a string representation of the medication details.
     * @return A string containing the medication's name, quantity, and supplier.
     */
    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
