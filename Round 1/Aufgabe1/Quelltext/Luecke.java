import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Die Klasse Luecke erstellt Lücken und speichert alle Attribute und Methoden zur Bestimmung aller möglichen Wörter,
 * die in sich passen. Diese Information wird wiederum von Lückenrätsel gebraucht, um das gesamte Rätsel zu lösen.
 *
 * @author Rui Zhang
 * @version 1.2
 */

public class Luecke {

    // alleSatzZeichen enthält alle geläufigen Satzzeichen, die es gibt und wird für die findeLoesungen() Methode benutzt (siehe unten)
    public static final LinkedList<Character> alleSatzZeichen;
    public static final char keinSatzZeichen;
    static {
        alleSatzZeichen = new LinkedList<Character>(){{
            add('!');
            add(',');
            add('.');
            add(':');
            add('?');
        }};
        keinSatzZeichen = '-';
    }

    private final String luecke;
    private final String[] alleWoerter;
    private final int lueckenIndex;
    private char satzZeichen;
    private String loesung;
    private final HashSet<String> moeglicheLoesungen;

    /**
     * Konstruktor ohne Parameter: wird benutzt, um "Platzhalter" Lückenobjekte zu erstellen, deren Lösung bzw. Wort später
     * gesetzt wird.
     */
    public Luecke() {
        this.luecke = null;
        this.alleWoerter = new String[] {};
        this.lueckenIndex = -1;
        this.satzZeichen = keinSatzZeichen;
        this.loesung = null;
        this.moeglicheLoesungen = null;
    }

    /**
     * @param luecke die Luecke die gegeben ist: bsp: __b___
     * @param alleWoerter alle Wörter, die zur Verfügung stehen
     * @param lueckenIndex die Position der Lücke in dem Lückentext
     */
    public Luecke(String luecke, ArrayList<String> alleWoerter, int lueckenIndex) {
        this.luecke = luecke;
        this.alleWoerter = new String[alleWoerter.size()];

        for (int i=0; i<alleWoerter.size(); i++) {
            this.alleWoerter[i] = alleWoerter.get(i);
        }

        this.lueckenIndex = lueckenIndex;
        this.satzZeichen = keinSatzZeichen;
        this.loesung = null;
        this.moeglicheLoesungen = new HashSet<>(this.findeLoesungen());
    }

    // Getter- und Setter-Methoden
    public int getLueckenIndex() { return this.lueckenIndex; }
    public char getSatzZeichen() { return this.satzZeichen; }
    public String getLoesung() { return this.loesung; }
    public HashSet<String> getMoeglicheLoesungen() { return this.moeglicheLoesungen; }

    public void setLoesung(String newLoesung) { this.loesung = newLoesung; }


    /**
     * @return eine Liste mit allen möglichen Woerter, die in diese Lücke eingesetzt werden können.
     */
    private LinkedList<String> findeLoesungen() {

        LinkedList<String> passendeWoerter = new LinkedList<>();

        String lLuecke = this.luecke;
        char lastChar = lLuecke.charAt(lLuecke.length()-1);

        if (alleSatzZeichen.contains(lastChar)) {
            this.satzZeichen = lastChar;
            lLuecke = lLuecke.substring(0, lLuecke.length()-1);
        }

        for (String wort : this.alleWoerter) {
            if (wort.length() == lLuecke.length() && this.lueckenWortCheck(lLuecke, wort)) {
                passendeWoerter.add(wort);
            }
        }

        return passendeWoerter;

    }

    /**
     * @param pLuecke die Lücke, die mit dem Wort verglichen wird
     * @param wort das Wort, womit die Lücke verglichen Wird
     *
     * @return Ob die Lücke und das gegebene Wort zusammenpassen.
     */
    private boolean lueckenWortCheck(String pLuecke, String wort) {
        for (int i=0; i<pLuecke.length(); i++) {
            if (wort.charAt(i) != pLuecke.charAt(i) && pLuecke.charAt(i) != '_') {
                return false;
            }
        }
        return true;
    }

}
