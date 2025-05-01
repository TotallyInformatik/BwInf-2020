import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Die Klasse LueckenRaetsel erstellt Lückenrätsel und speichert die benötigten Attribute zur Bestimmung der Lösung
 * ab. Sie enthält auch eine Methode, die sich selber löst.
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class LueckenRaetsel {

    private final ArrayList<String> woerter;
    private final ArrayList<Luecke> luecken;

    /**
     * @param path der Pfad zur Datei, die gelesen werden soll
     * @throws FileNotFoundException Wenn eine Datei mit dem gegebenen Pfad nicht existiert, erscheint dieser Fehler. Er wird in der Main Methode aufgefangen und behandelt
     * @throws NoSuchElementException Wenn eine Datei keinen Inhalt, oder nicht genug Zeilen besitzt, weil wahrscheinlich der Format der Beispieldatei nicht korrekt ist, so erscheint dieser Fehler aufgrund des "nextLine()" Befehls;
     */
    public LueckenRaetsel(String path) throws IOException, NoSuchElementException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8));

        String[] lueckenText = reader.readLine().trim().split(" ");
        this.woerter = new ArrayList<>(Arrays.asList(reader.readLine().split(" ")));
        this.luecken = new ArrayList<>();

        for (int i = 0; i < lueckenText.length; i++) {
            this.luecken.add(new Luecke(lueckenText[i], this.woerter, i));
        }

    }

    /**
     * @return die Lösung zum Lückentext
     */
    public String loeseRaetsel() {
        Luecke[] ausgefuellteLuecken = new Luecke[this.luecken.size()];
        for (int i = 0; i < ausgefuellteLuecken.length; i++) {
            ausgefuellteLuecken[i] = new Luecke();
        }

        // findet Lösungen für alle Lücken
        while (!this.luecken.isEmpty()) {

            for (int i = 0; i < this.luecken.size(); i++) {
                Luecke lueckenObjekt = this.luecken.get(i);
                if (lueckenObjekt.getMoeglicheLoesungen().size() == 1) {
                    ausgefuellteLuecken[lueckenObjekt.getLueckenIndex()] = lueckenObjekt;
                    lueckenObjekt.setLoesung(lueckenObjekt.getMoeglicheLoesungen().iterator().next());
                    this.luecken.remove(lueckenObjekt);
                    this.woerter.remove(lueckenObjekt.getLoesung());
                    i--;
                } else {
                    lueckenObjekt.getMoeglicheLoesungen().removeIf(loesung -> !this.woerter.contains(loesung));
                }
            }

        }

        // erstellt Ausgabe
        StringBuilder outputString = new StringBuilder();
        for (Luecke luecke : ausgefuellteLuecken) {
            if (luecke.getSatzZeichen() != Luecke.keinSatzZeichen)
                outputString.append(luecke.getLoesung()).append(luecke.getSatzZeichen()).append(" ");
            else
                outputString.append(luecke.getLoesung()).append(" ");

        }

        return outputString.toString();

    }

}
