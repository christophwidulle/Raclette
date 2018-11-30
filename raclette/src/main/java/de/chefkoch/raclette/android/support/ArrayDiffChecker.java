package de.chefkoch.raclette.android.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayDiffChecker {

    public class AdapterDiff {
        int diffCount;
        int diffPosition;

        public AdapterDiff(int diffCount, int diffPosition) {
            this.diffCount = diffCount;
            this.diffPosition = diffPosition;
        }

        public int getDiffCount() {
            return diffCount;
        }

        public int getDiffPosition() {
            return diffPosition;
        }
    }

    /**
     * Vergleicht zwei Arrays miteinander.
     * @param list1
     * @param list2
     * @return Ein Objekt vom Typ {@link ArrayDiffChecker.AdapterDiff}, folgende Ausprägungen kann
     * es dabei gehen:
     *  diffCount = 0  => Die Arrays sind genau gleich
     *  diffCount = 1  => List2 enthält genau ein Element mehr als List1
     *  diffCount = -1 => List2 enthält genau ein Element weniger als List1
     *
     *  Bei diffCount = 1 und diffCount = -1 wird in einer weiteren Variable diffPosition angegeben,
     *  an welcher Stelle ein Element hinzugekommen bzw. gelöscht wurde.
     *
     *  NULL wird zurück gegeben, wenn die Arrays mehr als zwei Unterschiede haben
     */
    public AdapterDiff getDiff(List list1, List list2) {

        // Die Listen sind komplett gleich
        if (Arrays.equals(list1.toArray(), list2.toArray())) {
            return new AdapterDiff(0, 0);
        }
        // Wenn die Listen nicht gleich sind, aber die gleiche Länge haben, können wir keine Change Animation fahren
        else if (list1.size() == list2.size()) {
            return null;
        }
        // List2 ist größer als List1, demnach wurde ein Element zu List2 hinzugefügt
        else if (list1.size() - list2.size() == -1) {
            List tempList2 = new ArrayList(list2);
            tempList2.removeAll(list1);

            if (tempList2.size() == 1) {
                Object item = tempList2.get(0);

                //Entferne das Item aus Liste2 und prüfe ob sie nun equals sind (hier wird die Reihenfolge beachtet)
                tempList2 = new ArrayList(list2);
                tempList2.remove(item);
                if (Arrays.equals(list1.toArray(), tempList2.toArray())) {

                    // Es hat sich wohl nur das eine Item geändert, nun müssen wir die Position suchen
                    int diffPosition = list2.indexOf(item);
                    return new AdapterDiff(1, diffPosition);
                }
            }
        }
        // List2 ist kleiner als List1, demnach wurde ein Element aus List2 entfernt
        else if (list1.size() - list2.size() == 1) {
            List tempList1 = new ArrayList(list1);
            tempList1.removeAll(list2);

            if (tempList1.size() == 1) {
                Object item = tempList1.get(0);

                //Entferne das Item aus Liste2 und prüfe ob sie nun equals sind (hier wird die Reihenfolge beachtet)
                tempList1 = new ArrayList(list1);
                tempList1.remove(item);
                if (Arrays.equals(tempList1.toArray(), list2.toArray())) {

                    // Es hat sich wohl nur das eine Item geändert, nun müssen wir die Position suchen
                    int diffPosition = list1.indexOf(item);
                    return new AdapterDiff(-1, diffPosition);
                }
            }
        }

        return null;
    }
}
