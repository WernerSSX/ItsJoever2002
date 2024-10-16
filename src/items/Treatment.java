package items;
import java.util.Scanner;

public class Treatment {
    private Prescription[] allPrescribedMedicine;
    private final String treatmentComments;
    
    public Treatment(int numPrescribedMedicine) {
        Scanner sc = new Scanner(System.in);
        int i = numPrescribedMedicine;
        while (i-1 >= 0) {
            System.out.printf("Please input the Medicine ID for Prescription %d\n", numPrescribedMedicine-i+1);
            int medicineID = sc.nextInt();
            sc.nextLine();
            // Medicine Name can be retrieved from Medicine
            System.out.printf("Please input the Medicine Quantity for Prescription %d\n", numPrescribedMedicine-i+1);
            int medicineQuantity = sc.nextInt();
            sc.nextLine();
            allPrescribedMedicine[numPrescribedMedicine-i] = new Prescription(medicineID, medicineQuantity);
            i--;
        }
        System.out.println("Please input some comments for this treatment:");
        treatmentComments = sc.next();
    }

    public void printAllPrescribedMedicine() {
        for (Prescription prescribedMedicine : allPrescribedMedicine) {
            // Prints out all of the prescribed medicine in terms of quantity, id, name
            System.out.printf("Medicine ID: %d\n", prescribedMedicine.getMedicineID());
            System.out.printf("Medicine Name: %s\n", prescribedMedicine.getMedicineName());
            System.out.printf("Medicine Quantity: %d \n\n", prescribedMedicine.getMedicineQuantity());
        }
    }

    public void printTreatmentComments() {
        System.out.printf("%s\n", treatmentComments);
    }
}
