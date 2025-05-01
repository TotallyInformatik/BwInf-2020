import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Die Klasse DreiecksPuzzle erstellt Dreieckspuzzlen, die Attribute und Methoden beinhalten, die zur Bestimmung der
 * Lösung des Dreieckspuzzles benötigt werden.
 *
 * Author: Rui Zhang
 * ver: 1.2
 */

public class DreiecksPuzzle {

    private final ArrayList<Dreieck> teile;
    private final HashMap<Integer, ArrayList<DreiecksNode>> alleSchichten;

    /**
     * @param path Der Pfad zur Beispieldatei
     * @throws FileNotFoundException Dieser Fehler taucht auf, wenn keine Datei mit dem Pfad existiert
     * @throws NoSuchElementException Dieser Fehler taucht zum Beispiel bei dem nextLine() Befehl auf, wenn die Datei nicht genug Inhalt besitzt
     */
    public DreiecksPuzzle(String path) throws IOException, NoSuchElementException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8));

        reader.readLine();
        reader.readLine();

        this.teile = new ArrayList<>();
        this.alleSchichten = new HashMap<>();

        String zeile;
        while ((zeile = reader.readLine()) != null) {
            this.teile.add(new Dreieck(zeile));
        }

    }

    // Getter-Methoden
    public HashMap<Integer, ArrayList<DreiecksNode>> getAlleSchichten() { return alleSchichten; }
    public ArrayList<Dreieck> getTeile() { return teile; }

    /**
     * Diese Methode ist der Startpunkt zur Bestimmung einer Lösung für das Puzzle, hierbei wird eine Rekursive Methode
     * "naechsteSchicht()" angewendet, um die Lösung zu bestimmen.
     *
     * @return ob das Puzzle lösbar ist.
     */
    public boolean loesePuzzle() {

        ArrayList<Dreieck> uebrigeTeile = new ArrayList<>(this.teile);
        for (Dreieck teil : uebrigeTeile) {

            DreiecksNode jetzigerDreiecksknoten = new DreiecksNode(teil);
            jetzigerDreiecksknoten.setPositionInSchicht(0);

            for (int seite=0; seite<3; seite++) {
                ArrayList<Integer> offeneSeiten = new ArrayList<>();
                offeneSeiten.add(seite);
                jetzigerDreiecksknoten.setOffeneSeiten(offeneSeiten);

                int jetzigeSchichtzahl = 0;
                ArrayList<DreiecksNode> schicht0 = new ArrayList<DreiecksNode>() {{
                    add(jetzigerDreiecksknoten);
                }};

                ArrayList<Dreieck> neueUebrigeTeile = new ArrayList<>(uebrigeTeile);
                neueUebrigeTeile.remove(teil);

                if (this.naechsteSchicht(jetzigeSchichtzahl + 1, neueUebrigeTeile, schicht0)) {
                    this.alleSchichten.put(jetzigeSchichtzahl, schicht0);
                    return true;
                }

            }

        }

        return false;

    }

    /**
     * @param schichtZahl index der Schicht
     * @param positionInSchicht Index / Position des jetzigen Knotens
     * @param vorherigeSchicht vorherige Schicht (alle Knoten in der vorherigen Schicht)
     * @return alle Eltern des jetzigen Knotens (können zwei oder eins sein)
     */
    private ArrayList<DreiecksNode> getEltern(int schichtZahl, int positionInSchicht, ArrayList<DreiecksNode> vorherigeSchicht) {
        ArrayList<DreiecksNode> eltern = new ArrayList<>();

        if (schichtZahl % 2 == 0) {
            // es wird möglicherweise zwei Elternteile geben

            // Bestimmung des rechten und linken Elternteils falls vorhanden
            if (positionInSchicht - 1 >= 0) {
                DreiecksNode eltern0 = vorherigeSchicht.get(positionInSchicht - 1);
                eltern.add(eltern0);
            }
            if (positionInSchicht < vorherigeSchicht.size()) {

                DreiecksNode eltern1 = vorherigeSchicht.get(positionInSchicht);
                eltern.add(eltern1);
            }

        } else {
            // es wird nur ein Elternteil geben
            eltern.add(vorherigeSchicht.get(positionInSchicht));
        }

        return eltern;
    }

    /**
     * Durch das nutzen der Informationen der Elternknoten und der Position des Knotens werden alle möglichen Dreiecke,
     * die eingesetzt werden können, bestimmt.
     *
     * @param elternteile die "Eltern" des jetzigen Knotens
     * @param uebrigeTeile alle Dreiecke, die übrig sind und potenziell eingesetzt werden können
     * @param positionInSchicht die Position, an der der Knoten in seiner Schicht positioniert ist.
     * @return alle möglichen Dreiecke, die in dem jetzigen Knoten eingesetzt werden können.
     */
    private ArrayList<Pair<Dreieck, ArrayList<Integer>>> getAlleMoeglichenWerteFuerKnoten(ArrayList<DreiecksNode> elternteile, ArrayList<Dreieck> uebrigeTeile, int positionInSchicht) {

        ArrayList<Pair<Dreieck, ArrayList<Integer>>> moeglicheWerte = new ArrayList<>();

        for (Dreieck teil : uebrigeTeile) {
            if (elternteile.size() == 1) {

                // Elternteile.size() == 1 wenn Dreieck direkt unterhalb einem Dreieck ist, oder wenn das Dreieck rechts oder links ist.

                DreiecksNode dreiecksknotenVonElternteil = elternteile.get(0);

                // bestimmt die Seite, die das Elternteil in Richtung des Dreiecks zeigt
                ArrayList<Integer> offeneSeitenVonEltern = dreiecksknotenVonElternteil.getOffeneSeiten();
                int offeneSeite = offeneSeitenVonEltern.get(positionInSchicht - dreiecksknotenVonElternteil.getPositionInSchicht());

                // Bestimmung der Figurenhälfte, die das Elternteil in Richtung des neuen Dreiecks zeigt
                int offeneFigurenHaelften = dreiecksknotenVonElternteil.getWert().getFigurenHaelften().get(offeneSeite);
                ArrayList<Integer> figurenHaelftenVonDreieck = teil.getFigurenHaelften();

                // probiert alle Ausrichtungen von dem Dreieck aus.
                for (int seite = 0; seite < 3; seite++) {
                    if (figurenHaelftenVonDreieck.get(seite) == offeneFigurenHaelften * -1) {

                        ArrayList<Integer> alleOffeneSeiten = new ArrayList<>();

                        if (dreiecksknotenVonElternteil.getPositionInSchicht() == positionInSchicht) {

                            if (offeneSeitenVonEltern.size() == 2) {
                                // das heißt, dass der Dreiecksknoten links von dem Elternteil ist

                                // Bestimmung der Seite, die das Dreieck in Richtung der nächsten Schicht zeigen wird
                                alleOffeneSeiten.add(seite + 1 - (Math.floorDiv(seite, 2) * 3));

                            } else {
                                // das heißt, dass der Dreiecksknoten unter dem Elternteil ist

                                // Bestimmung der Seite, die das Dreieck in Richtung der nächsten Schicht zeigen wird
                                int rechteOffeneSeite = seite + 1 - (Math.floorDiv(seite, 2) * 3);
                                int linkeOffeneSeite = seite + 2 - (Math.floorDiv(seite + 1, 2) * 3);

                                alleOffeneSeiten.add(linkeOffeneSeite);
                                alleOffeneSeiten.add(rechteOffeneSeite);

                            }

                        } else if (dreiecksknotenVonElternteil.getPositionInSchicht() == positionInSchicht - 1) {
                            // das heißt, dass der Dreiecksknoten rechts von dem Elternteil ist

                            // Bestimmung der Seite, die das Dreieck in Richtung der nächsten Schicht zeigen wird
                            int neueOffeneSeite = seite - 1;
                            if (neueOffeneSeite < 0) {
                                neueOffeneSeite += 3;
                            }
                            alleOffeneSeiten.add(neueOffeneSeite);

                        }

                        // Hinzufügen zu allen Wertepaaren (alle möglichen einzusetzenden Dreiecke / Werte und deren Ausrichtung)
                        Pair<Dreieck, ArrayList<Integer>> wertePaare = new Pair<>(teil, alleOffeneSeiten);
                        moeglicheWerte.add(wertePaare);

                    }
                }


            } else if (elternteile.size() == 2) {

                // Elternteile.size() == 2 wenn Dreieck sich zwischen zwei oberen Dreiecken befindet

                // Bestimmung der Dreiecke
                DreiecksNode elternteil0 = elternteile.get(0);
                DreiecksNode elternteil1 = elternteile.get(1);

                // Bestimmung der Figurenhälften, die in Richtung des Knoten gezeigt wird
                int linkesElternteilOffeneSeite = elternteil0.getOffeneSeiten().get(1);
                int rechtesElternteilOffeneSeite = elternteil1.getOffeneSeiten().get(0);
                int linkesElternteilOffeneFigurenHaelfte = elternteil0.getWert().getFigurenHaelften().get(linkesElternteilOffeneSeite);
                int rechtesElternteilOffeneFigurenHaelfte = elternteil1.getWert().getFigurenHaelften().get(rechtesElternteilOffeneSeite);

                ArrayList<Integer> figurenHaelftenVonDreieck = teil.getFigurenHaelften();

                // Ausprobieren aller Ausrichtungen.
                for (int seiteLinks = 0, seiteRechts = 1; seiteLinks < 3; seiteLinks++, seiteRechts++) {
                    if (seiteRechts == 3) {
                        seiteRechts -= 3;
                    }

                    if ((figurenHaelftenVonDreieck.get(seiteLinks) == linkesElternteilOffeneFigurenHaelfte * -1) &&
                            (figurenHaelftenVonDreieck.get(seiteRechts) == rechtesElternteilOffeneFigurenHaelfte * -1)) {

                        ArrayList<Integer> alleOffeneSeiten = new ArrayList<>();
                        alleOffeneSeiten.add(2 - (seiteLinks + seiteRechts - 1));

                        Pair<Dreieck, ArrayList<Integer>> currentValuePair = new Pair<>(teil, alleOffeneSeiten);
                        moeglicheWerte.add(currentValuePair);
                    }

                }

            }
        }

        return moeglicheWerte;

    }

    /**
     * @param jetzigeKombination jetzige Kombination (wird benutzt, ob die Kombination fertigzustellen)
     * @param jetzigeSchicht jetzige Schicht (alle Knoten in dieser Schicht)
     * @param schichtZahl jetige Index für die Schicht
     * @param uebrigeTeile alle Dreiecke, die noch eingesetzt werden können.
     * @return ob überhaupt eine Kombination mit der jetzigen Anordnung der Schicht erstellt werden kann, die eine Lösung für weitere Schichten anbietet
     */
    private boolean probiereAlleKombinationen(ArrayList<DreiecksNode> jetzigeKombination, ArrayList<DreiecksNode> jetzigeSchicht, int schichtZahl, ArrayList<Dreieck> uebrigeTeile) {
        DreiecksNode jetzigerKnoten = jetzigeSchicht.get(0);
        ArrayList<DreiecksNode> neueSchicht = new ArrayList<>(jetzigeSchicht);
        neueSchicht.remove(0);

        for (Pair<Dreieck, ArrayList<Integer>> wertePaar: jetzigerKnoten.getAlleMoeglichenDreiecke()) {

            Dreieck dreieckVonWertePaar = wertePaar.getKey();

            if (uebrigeTeile.contains(dreieckVonWertePaar)) {
                ArrayList<DreiecksNode> neueKombination = new ArrayList<>(jetzigeKombination);
                neueKombination.add(new DreiecksNode(jetzigerKnoten.getPositionInSchicht(), dreieckVonWertePaar, wertePaar.getValue()));
                ArrayList<Dreieck> neueUebrigeTeile = new ArrayList<>(uebrigeTeile);
                neueUebrigeTeile.remove(dreieckVonWertePaar);

                if (neueSchicht.size() == 0) {

                    if (this.naechsteSchicht(schichtZahl + 1, neueUebrigeTeile, neueKombination)) {
                        this.alleSchichten.put(schichtZahl, neueKombination);
                        return true;
                    }

                } else {
                    return probiereAlleKombinationen(neueKombination, neueSchicht, schichtZahl, neueUebrigeTeile);
                }
            }

        }

        return false;

    }

    /**
     * Die Methode naechsteSchicht() ermittelt aus den übrigenTeile und der vorherigen Schicht möglichen Dreiecke für alle
     * Dreiecksknoten in dieser Schicht des Dreieckspuzzles. Hierbei wird die tryAllPermutations() Methode benutzt, um
     * alle möglichen Kombinationen von Dreiecke in den Dreiecksknoten auszuprobieren und zu gucken, ob diese Kombination
     * eine Lösung für die nächste Schicht anbietet. Falls ja, dann wird true zurückgegeben.
     *
     * @param schichtZahl die jetzige Schicht
     * @param uebrigeTeile alle übrigen Dreiecke, die eingesetzt werden kann
     * @param vorherigeSchicht die vorherige Schicht
     * @return ob es hierfür eine Lösung gibt, oder nicht.
     */
    private boolean naechsteSchicht(int schichtZahl, ArrayList<Dreieck> uebrigeTeile, ArrayList<DreiecksNode> vorherigeSchicht) {

        // Wenn das Puzzle gelöst ist, dann gibt es keine weitere Schicht mehr -> Abbruch des rekursiven Algorithmus
        if (uebrigeTeile.size() == 0) {
            return true;
        }

        ArrayList<DreiecksNode> jetzigeSchicht = new ArrayList<>();
        int anzahlDerKnoten = (int) Math.ceil((double) (schichtZahl+1) / 2); // berechnet Anzahl der Knoten, die zu dieser Schicht hinzugefügt müssen
        for (int i=0; i<anzahlDerKnoten; i++) {

            // erstellt Knoten, findet die Elternkonten heraus, nutzt diese Information um alle möglchen Werte zu bestimmen
            DreiecksNode jetzigerDreiecksKnoten = new DreiecksNode();
            jetzigerDreiecksKnoten.setPositionInSchicht(i);
            ArrayList<DreiecksNode> elternVonDreiecksKnoten = this.getEltern(schichtZahl, i, vorherigeSchicht);

            ArrayList<Pair<Dreieck, ArrayList<Integer>>> alleMoeglichenWerteFuerKnoten = this.getAlleMoeglichenWerteFuerKnoten(elternVonDreiecksKnoten, uebrigeTeile, i);

            jetzigerDreiecksKnoten.setAlleMoeglichenDreiecke(alleMoeglichenWerteFuerKnoten);
            jetzigeSchicht.add(jetzigerDreiecksKnoten);

        }

        return probiereAlleKombinationen(new ArrayList<>(), jetzigeSchicht, schichtZahl, uebrigeTeile);

    }

}
