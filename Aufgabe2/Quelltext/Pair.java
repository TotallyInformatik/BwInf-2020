
/**
 * Die Pair Klasse erstellt Schlüssel-Werte-Paare
 *
 * Author: Rui Zhang
 * ver: 1.2
 */

public class Pair<K, V> {

    private final K key;
    private final V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
}
