import java.io.IOException;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;

/**
 * Die Main Klasse ist die Klasse, welche die main-Methode enthält und ist der Einstiegspunkt
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class Main {

    public static void main(String[] args) {

        StringBuilder anleitung = new StringBuilder();
        anleitung
                .append("BWINF Aufgabe 1:\n")
                .append("Geben Sie bitte eine Zahl ein (0, 1, 2, 3 oder 4).\n")
                .append("Die angegebene Beispieldatei wird dann benutzt und das Ergebnis wird ausgegeben.\n")
                .append("Es wurden hierbei die Beispieldateien von der BWINF Webseite übernommen.");

        JOptionPane.showMessageDialog(null, anleitung);

        String eingabe = JOptionPane.showInputDialog("Bitte geben sie jetzt eine Zahl ein: ");

        try {

            LueckenRaetsel lueckenRaetsel = new LueckenRaetsel("Beispiel" + eingabe + ".txt");
            String loesung = lueckenRaetsel.loeseRaetsel();
            JOptionPane.showMessageDialog(null, "Ausgabe / Lösung: " + loesung);

        } catch (IOException | NullPointerException | NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Beispieldatei existiert nicht, bitte geben sie: 0, 1, 2, 3, oder 4 ein!");
        }

    }

}
