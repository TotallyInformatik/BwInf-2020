import java.util.ArrayList;

/**
 * Die Klasse DreiecksNode wird bei der Lösungsbestimmung vom Puzzle gebraucht, da sie abspeichert, an welcher Stelle sie
 * im Puzzle platziert ist, welche möglichen Dreiecke sie abspeichern kann und welche Orientierung das Dreieck, was letztendlich
 * eingesetzt wird, besitzt.
 *
 * Author: Rui Zhang
 * ver: 1.2
 */

public class DreiecksNode {

    private Dreieck wert;
    private ArrayList<Pair<Dreieck, ArrayList<Integer>>> alleMoeglichenDreiecke;
    private int positionInSchicht;
    private ArrayList<Integer> offeneSeiten;

    public DreiecksNode(Dreieck wert) {
        this();
        this.wert = wert;
    }
    public DreiecksNode() {
        this.positionInSchicht = -1;
        this.offeneSeiten = null;
        this.wert = null;
        this.alleMoeglichenDreiecke = null;
    }
    public DreiecksNode(int positionInSchicht, Dreieck wert, ArrayList<Integer> offeneSeiten) {
        this();
        this.positionInSchicht = positionInSchicht;
        this.wert = wert;
        this.offeneSeiten = offeneSeiten;
    }

    public Dreieck getWert() { return wert; }

    public ArrayList<Integer> getOffeneSeiten() { return this.offeneSeiten; }
    public void setOffeneSeiten(ArrayList<Integer> seiten) { this.offeneSeiten = seiten; }

    public int getPositionInSchicht() { return this.positionInSchicht; }
    public void setPositionInSchicht(int index) { this.positionInSchicht = index; }

    public ArrayList<Pair<Dreieck, ArrayList<Integer>>> getAlleMoeglichenDreiecke() { return this.alleMoeglichenDreiecke; }
    public void setAlleMoeglichenDreiecke(ArrayList<Pair<Dreieck, ArrayList<Integer>>> alleMoeglichenDreiecke) { this.alleMoeglichenDreiecke = alleMoeglichenDreiecke; }

}
