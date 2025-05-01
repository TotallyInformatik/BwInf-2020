import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Die Klasse RNG erstellt ein Objekt, das Tobis Turnier über mehrere Wiederholungen simuliert und Tobi eine Empfehlung
 * gibt, welche Turniervariante er nutzen sollte, damit der spielstärkste Spieler am wahrscheinlichsten gewinnt.
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class RNG {

    private final int anzahlWiederholungen;
    private final ArrayList<Spieler> spieler;
    private Spieler staerksterSpieler;

    /**
     * @param path der Pfad zu der Beispieldatei, welche benutzt wird.
     * @param anzahlWiederholungen die Anzahl der Wiederholungen, die bei jeder Turniervariante ausgeführt werden soll.
     * @throws FileNotFoundException Dieser Fehler taucht auf, wenn keine Datei mit dem gegebenen Pfad existiert.
     */
    public RNG(String path, int anzahlWiederholungen) throws IOException, NullPointerException {
        this.anzahlWiederholungen = anzahlWiederholungen;
        this.staerksterSpieler = new Spieler(0, 0);
        this.spieler = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8));
        reader.readLine();

        int i = 0;
        String zeile;
        while ((zeile = reader.readLine()) != null) {
            i++;
            Spieler jetzigerSpieler = new Spieler(i, Integer.parseInt(zeile));
            this.spieler.add(jetzigerSpieler);
            if (jetzigerSpieler.getSpielStaerke() > this.staerksterSpieler.getSpielStaerke()) {
                this.staerksterSpieler = jetzigerSpieler;
            }
        }

    }

    /**
     * Die siegeReset Methode wird bei der Turniervariante Liga benutzt, um nach jeder Turniersimulation die Siege zurückzusetzen.
     */
    private void siegeReset() {
        for (Spieler spieler : this.spieler) {
            spieler.setAnzahlSiege(0);
        }
    }

    /**
     * @param ersterSpieler der erste Spieler, der zieht
     * @param zweiterSpieler der zweite Spieler, der zieht
     * @return der Spieler, der gemäß Tobis Zufallexperiment gewinnt.
     */
    private Spieler ziehen(Spieler ersterSpieler, Spieler zweiterSpieler) {
        int gezogen = new Random().nextInt(ersterSpieler.getSpielStaerke() + zweiterSpieler.getSpielStaerke() + 1);
        return gezogen < ersterSpieler.getSpielStaerke() ? ersterSpieler : zweiterSpieler;
    }

    /**
     * @return die Anzahl der Gewinne des spielstärksten Spielers bei der Turniervariante Liga.
     */
    private int liga() {
        int anzahlGewinneStaerksterSpieler = 0;
        for (int i=0; i<this.anzahlWiederholungen; i++) {
            Spieler meisteGewinne = this.spieler.get(0);
            int startIndex = 0;

            for (Spieler ersterSpieler : this.spieler) {
                for (Spieler zweiterSpieler : this.spieler.subList(startIndex, this.spieler.size())) {
                    Spieler gewinner = this.ziehen(ersterSpieler, zweiterSpieler);
                    gewinner.siegeInkrement();

                    if ((gewinner.getAnzahlSiege() > meisteGewinne.getAnzahlSiege()) ||
                        gewinner.getAnzahlSiege() == meisteGewinne.getAnzahlSiege() &&
                        gewinner.getNummer() < meisteGewinne.getNummer())
                        meisteGewinne = gewinner;
                }

                startIndex++;
            }

            this.siegeReset();

            if (meisteGewinne == this.staerksterSpieler)
                anzahlGewinneStaerksterSpieler++;

        }

        return anzahlGewinneStaerksterSpieler;
    }

    /**
     * @return die Anzahl der Gewinne des spielstärksten Spielers bei der Turniervariante KO.
     */
    private int KO() {

        int anzahlGewinneStaerksterSpieler = 0;
        for (int i=0; i<this.anzahlWiederholungen; i++) {

            Collections.shuffle(this.spieler);
            Spieler gewinner = KORunde(this.spieler);

            if (gewinner == this.staerksterSpieler)
                anzahlGewinneStaerksterSpieler++;

        }

        return anzahlGewinneStaerksterSpieler;

    }

    /**
     * @param spieler Spielerliste, die in dieser Rekursion mitmachen
     * @return der Spieler, der gewonnen hat.
     */
    private Spieler KORunde(List<Spieler> spieler) {

        if (spieler.size() != 2)
            return this.ziehen(
                    this.KORunde(spieler.subList(0, spieler.size() / 2)),
                    this.KORunde(spieler.subList(spieler.size() / 2, spieler.size())));
        else
            return this.ziehen(spieler.get(0), spieler.get(1));

    }

    /**
     * @return die Anzahl der Gewinne des spielstärksten Spielers bei der Turniervariante KO mal 5.
     */
    private int KOMal5() {

        int anzahlGewinneStaerksterSpieler = 0;
        for (int i=0; i<this.anzahlWiederholungen; i++) {

            Collections.shuffle(this.spieler);
            Spieler gewinner = KOMal5Runde(this.spieler);

            if (gewinner == this.staerksterSpieler)
                anzahlGewinneStaerksterSpieler++;

        }

        return anzahlGewinneStaerksterSpieler;

    }

    /**
     * @param spieler Spielerliste, die in dieser Rekursion mitmachen
     * @return der Spieler, der gewonnen hat
     */
    private Spieler KOMal5Runde(List<Spieler> spieler) {

        Spieler ersterSpieler, zweiterSpieler;
        if (spieler.size() != 2) {
            ersterSpieler = this.KOMal5Runde(spieler.subList(0, spieler.size() / 2));
            zweiterSpieler = this.KOMal5Runde(spieler.subList(spieler.size() / 2, spieler.size()));
        } else {
            ersterSpieler = spieler.get(0);
            zweiterSpieler = spieler.get(1);
        }

        ersterSpieler.setAnzahlSiege(0);
        zweiterSpieler.setAnzahlSiege(0);

        for (int i=0; i<5; i++) {
            this.ziehen(ersterSpieler, zweiterSpieler).siegeInkrement();
        }

        return ersterSpieler.getAnzahlSiege() > zweiterSpieler.getAnzahlSiege() ? ersterSpieler : zweiterSpieler;

    }

    /**
     * Diese Methode ist die Hauptmethode dieser Aufgabe.
     * Alle weitern Methoden werden benutzt, um die Anzahl der Siege des spielstärksten Spielers für jede Turniervariante zu bestimmen.
     * Je nach dem, bei welcher Turniervariante der spielstärkster Spieler am meisten gewinnt, wird eine Empfehlung für
     * Tobi gegeben.
     */
    public void turnierVariantenTest() {

        int richtigeLigaSiege = this.liga();
        int richtigeKOSiege = this.KO();
        int richtigeKOMal5Siege = this.KOMal5();
        HashMap<Integer, String> siegeZuTurnierVariante = new HashMap<Integer, String>(){{
            put(richtigeLigaSiege, "Liga");
            put(richtigeKOSiege, "KO");
            put(richtigeKOMal5Siege, "KO*5");
        }};

        StringBuilder ausgabe = new StringBuilder();
        ausgabe
            .append("Simulationsergebnisse:\n")
            .append("Liga: spielstärkster Spieler hat ").append(richtigeLigaSiege).append(" mal aus ").append(this.anzahlWiederholungen).append(" gewonnen\n")
            .append("KO: spielstärkster Spieler hat ").append(richtigeKOSiege).append(" mal aus ").append(this.anzahlWiederholungen).append(" gewonnen\n")
            .append("KO*5: spielstärkster Spieler hat ").append(richtigeKOMal5Siege).append(" mal aus ").append(this.anzahlWiederholungen).append(" gewonnen\n")
            .append("Empfehlung für Tobi: ").append(siegeZuTurnierVariante.get(Math.max(richtigeLigaSiege, Math.max(richtigeKOSiege, richtigeKOMal5Siege))));

        JOptionPane.showMessageDialog(null, ausgabe);

    }

}
