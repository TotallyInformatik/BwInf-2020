/**
 * Die Klasse Spieler speichert alle Attribute eines Spielers in Tobis Turnier.
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class Spieler {

    private final int nummer;
    private final int spielStaerke;
    private int anzahlSiege;

    /**
     * @param nummer Die Spielernummer des Spielers
     * @param spielStaerke Die Spielstärke des Spielers
     */
    public Spieler(int nummer, int spielStaerke) {
        this.nummer = nummer;
        this.spielStaerke = spielStaerke;
        this.anzahlSiege = 0;
    }

    public int getAnzahlSiege() { return anzahlSiege; }
    public int getNummer() { return nummer; }
    public int getSpielStaerke() { return spielStaerke; }

    public void siegeInkrement() { anzahlSiege++; }
    public void setAnzahlSiege(int anzahl) { anzahlSiege = anzahl; }

}
