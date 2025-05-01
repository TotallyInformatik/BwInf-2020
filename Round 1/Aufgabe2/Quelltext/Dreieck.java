import java.util.ArrayList;
import java.util.Objects;

/**
 * Die Klasse Dreieck erstellt Dreiecke, die vorhandenen Figurenhälften und ein ID wird abgespeichert.
 *
 * Author: Rui Zhang
 * ver: 1.2
 */

public class Dreieck {

    private static int IDZaehler;
    static {
        IDZaehler = 1;
    }

    private final ArrayList<Integer> figurenHaelften;
    private final int id;

    public Dreieck(String zeile) {
        this.figurenHaelften = new ArrayList<>();
        for (String part : zeile.trim().split(" ")) {
            this.figurenHaelften.add(Integer.parseInt(part));
        }
        this.id = IDZaehler;
        IDZaehler++;
    }

    // hashcode und equals Methoden zur Versicherung der richtigen Nutzung von .contains() Methoden bei ArrayLists
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dreieck dreieck = (Dreieck) o;
        return id == dreieck.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getter-Methoden
    public int getId() { return id; }
    public ArrayList<Integer> getFigurenHaelften() { return this.figurenHaelften; }

}
