import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Die Main Klasse enthält die main-Methode und ist der Einstiegspunkt
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class Main {

    public static void main(String[] args) {

        StringBuilder anleitung = new StringBuilder();

        anleitung
                .append("BWINF Aufgabe 3:\n")
                .append("Geben Sie bitte eine Zahl ein (0, 1, 2 oder 3).\n")
                .append("Die angegebene Beispieldatei wird dann benutzt und das Ergebnis wird ausgegeben.\n")
                .append("Es wurden hierbei die Beispieldateien von der BWINF Webseite übernommen.\n");


        JOptionPane.showMessageDialog(null, anleitung);

        String beispielIndex = JOptionPane.showInputDialog("Bitte geben Sie jetzt eine Zahl an: ");
        int anzahlWiederholungen = 1000;
        boolean eingabeFehlhaft = true;
        while (eingabeFehlhaft) {
            try {
                anzahlWiederholungen = Integer.parseInt(JOptionPane.showInputDialog("Bitte geben Sie jetzt die Anzahl der Wiederholungen an: "));
                eingabeFehlhaft = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Bitte geben sie eine Ganzzahl für die Anzahl der Wiederholungen an!");
            }
        }

        try {
            new RNG("Beispiel" + beispielIndex + ".txt", anzahlWiederholungen).turnierVariantenTest();
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Beispieldatei existiert nicht, bitte geben sie: 0, 1, 2, oder 3 ein!");
        }
    }

}
