package de.chefkoch.raclette.android.support;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class ArrayDiffCheckerTest {

    @Test
    public void testArraysAreEquals() {
        String[] list1 = {"A", "B", "C", "D"};
        String[] list2 = {"A", "B", "C", "D"};

       ArrayDiffChecker.AdapterDiff diff = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list2));

        assertTrue(diff.getDiffCount() == 0);
    }

    @Test
    public void testArraysNotEquals() {
        String[] list1 = {"A", "B", "C", "D"};
        String[] list2 = {"C", "D", "E", "D"};

        ArrayDiffChecker.AdapterDiff diff = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list2));

        assertTrue(diff == null);
    }

    @Test
    public void testItemInsertedToArray() {
        String[] list1 = {"A", "B", "C", "D"};
        String[] list2 = {"A", "B", "C", "D", "E"};
        String[] list3 = {"E", "A", "B", "C", "D"};
        String[] list4 =  {"A", "E", "B", "C", "D"};

        ArrayDiffChecker.AdapterDiff diff = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list2));
        assertNotNull(diff);
        assertTrue(diff.getDiffCount() == 1);
        assertTrue(diff.getDiffPosition() == 4);

        ArrayDiffChecker.AdapterDiff diff2 = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list3));
        assertNotNull(diff2);
        assertTrue(diff2.getDiffCount() == 1);
        assertTrue(diff2.getDiffPosition() == 0);

        ArrayDiffChecker.AdapterDiff diff3 = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list4));
        assertNotNull(diff3);
        assertTrue(diff3.getDiffCount() == 1);
        assertTrue(diff3.getDiffPosition() == 1);
    }

    @Test
    public void testItemRemovedFromArray() {
        String[] list1 = {"A", "B", "C", "D"};
        String[] list2 = {"A", "B", "C"};
        String[] list3 = {"B", "C", "D"};
        String[] list4 = {"A", "B", "D"};

        ArrayDiffChecker.AdapterDiff diff = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list2));
        assertNotNull(diff);
        assertTrue(diff.getDiffCount() == -1);
        assertTrue(diff.getDiffPosition() == 3);

        ArrayDiffChecker.AdapterDiff diff2 = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list3));
        assertNotNull(diff2);
        assertTrue(diff2.getDiffCount() == -1);
        assertTrue(diff2.getDiffPosition() == 0);

        ArrayDiffChecker.AdapterDiff diff3 = new ArrayDiffChecker().getDiff(Arrays.asList(list1), Arrays.asList(list4));
        assertNotNull(diff3);
        assertTrue(diff3.getDiffCount() == -1);
        assertTrue(diff3.getDiffPosition() == 2);
    }
}
