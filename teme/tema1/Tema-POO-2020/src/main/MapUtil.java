package main;

import java.util.Map;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * @beginnersbook.com
 */
public final class MapUtil {

    private MapUtil() {

    }

    /**
     * @param map map to be sorted
     * @param <K> type of key
     * @param <V> type of value
     * @return map sorted by values
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(final K k1, final K k2) {
                int compare = map.get(k1).compareTo(map.get(k2));
                if (compare == 0) {
                    return 1;
                } else {
                    return compare;
                }
            }
        };

        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

}

