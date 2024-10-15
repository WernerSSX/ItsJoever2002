package items;
import java.util.Scanner;

public class Diagnosis {
    private String[] diagnosedIllness;
    private String[] diagnosisComments;

    public Diagnosis(int numIllnesses) {
        Scanner sc = new Scanner(System.in);
        int i = numIllnesses;
        while (i-1 >= 0) {
            System.out.printf("Please write down the illness for Diagnosis %d", numIllnesses-i+1);
            String illness = sc.next();
            System.out.printf("Please write down the comments for Diagnosis %d", numIllnesses-i+1);
            String illnessComments = sc.next();
            diagnosedIllness[numIllnesses-i] = illness;
            diagnosisComments[numIllnesses-i] = illnessComments;
        }
    }

    public void printDiagnosedIllness() {
        int i = 1;
        for (String dianosed_illness : diagnosedIllness) {
            System.out.printf("Patient's Illness %d is %s\n", i, dianosed_illness);
            i++;
        }
    }

    public void printDiagnosedIllnessWithComments() {
        int i = 1;
        for (String dianosed_illness : diagnosedIllness) {
            System.out.printf("Patient's Illness %d is %s\n", i, dianosed_illness);
            System.out.printf("Comments for this illness: %s\n\n", diagnosisComments[i-1]);
            i++;
        }
    }

    // Add exception classes
}
