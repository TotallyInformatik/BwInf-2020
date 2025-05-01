import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Die Main Klasse ist der Einstiegspunkt des Programms, behandelt GUI.
 *
 * Author: Rui Zhang
 * ver: 1.2
 */

public class Main {

    public static void main(String[] args) {

        StringBuilder anleitung = new StringBuilder();
        anleitung
                .append("BWINF Aufgabe 2:\n")
                .append("Geben Sie bitte eine Zahl ein (0, 1, 2, oder 3).\n")
                .append("Die angegebene Beispieldatei wird dann benutzt und das Ergebnis wird ausgegeben.\n")
                .append("Es wurden hierbei die Beispieldateien von der BWINF Webseite übernommen.");
        JOptionPane.showMessageDialog(null, anleitung);

        String eingabe = JOptionPane.showInputDialog("Bitte geben Sie jetzt eine Zahl an: ");

        try {
            DreiecksPuzzle dreiecksPuzzle = new DreiecksPuzzle("Beispiel" + eingabe + ".txt");

            if (dreiecksPuzzle.loesePuzzle()) {

                StringBuilder ausgabe = new StringBuilder();

                ausgabe.append("Das Puzzle ist lösbar\n");
                for (Dreieck teil : dreiecksPuzzle.getTeile()) {
                    ausgabe.append(teil.getId()).append(": ").append(teil.getFigurenHaelften()).append("\n");
                }
                ausgabe.append("\n");

                HashMap<Integer, ArrayList<DreiecksNode>> solutionLayers = dreiecksPuzzle.getAlleSchichten();

                // Ausgabe zur Darstellung von der Lösung
                for (int layer = 0; layer <= solutionLayers.size() - 1; layer += 2) {
                    ArrayList<DreiecksNode> currentLayer = solutionLayers.get(layer);
                    ArrayList<DreiecksNode> prevLayer = solutionLayers.get(layer - 1);

                    if (prevLayer == null) {
                        prevLayer = new ArrayList<>();
                    }

                    StringBuilder layerAsString = new StringBuilder();

                    for (int spaceCount = 0; spaceCount < (solutionLayers.size() - 1) / 2 - (layer / 2); spaceCount++) {
                        layerAsString.append("   ");
                    }

                    for (int layerIndex = 0; layerIndex < currentLayer.size(); layerIndex++) {

                        layerAsString.append(currentLayer.get(layerIndex).getWert().getId()).append(" ");
                        if (layerIndex < prevLayer.size()) {
                            layerAsString.append(prevLayer.get(layerIndex).getWert().getId()).append(" ");
                        }
                    }
                    ausgabe.append(layerAsString).append("\n");
                }

                JOptionPane.showMessageDialog(null, ausgabe);

            } else {
                JOptionPane.showMessageDialog(null, "Das Puzzle ist nicht lösbar");
            }

        } catch (IOException | NullPointerException | NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Beispieldatei existiert nicht, bitte geben sie: 0, 1, 2, oder 3 ein!");
        }

    }

}
