package main;

import java.util.Map;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Function inspired from beginnersbook.com
 */
public final class MapUtil {

    private MapUtil() {

    }

    /**
     * Function returns a map sorted by values
     * @param map map to be sorted
     * @param <K> type of key
     * @param <V> type of value
     * @return map sorted by values
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = (k1, k2) -> {
            int compare = map.get(k1).compareTo(map.get(k2));
            if (compare == 0) {
                return 1;
            } else {
                return compare;
            }
        };

        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

}

