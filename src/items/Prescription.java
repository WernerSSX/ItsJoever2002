package items;

public class Prescription {
    private  int medicineID;
    private  String medicineName;
    private  int medicineQuantity;
    private  String medicineInstructions;

    public Prescription(int medicine_id, int medicine_quantity) {
        medicineID = medicine_id;
        // medicineName = [get from database]
        medicineQuantity = medicine_quantity;
        // medicineInstructions = [get from database]
    }

    public int getMedicineID() {
        return medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getMedicineQuantity() {
        return medicineQuantity;
    }

    public String getMedicineInstructions() {
        return medicineInstructions;
    }

    // Add exception classes
}
