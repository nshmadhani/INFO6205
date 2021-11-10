package edu.neu.coe.info6205.sort.parsort;
import edu.neu.coe.info6205.sort.par.ParSort;
import edu.neu.coe.info6205.util.CustomOrder;
import org.junit.BeforeClass;
import org.junit.Test;;
import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;

public class ParSortTest {
    @BeforeClass
    public static void setupClass() {

    }

    @Test
    public void testRandomOrder() {
        int[] sorted = CustomOrder.randomOrder(100000);
        int[] expected = new int[100000];
        System.arraycopy(sorted, 0, expected, 0, sorted.length);
        ParSort sort = new ParSort(50000, 100, false);
        sort.sort(sorted, 0, sorted.length);
        Arrays.parallelSort(expected);
        assertArrayEquals(expected, sorted);
    }

    @Test
    public void testPartialOrder() {
        int[] sorted = CustomOrder.patialOrder(100000);
        int[] expected = new int[100000];
        System.arraycopy(sorted, 0, expected, 0, sorted.length);
        ParSort sort = new ParSort(50000, 100, false);
        sort.sort(sorted, 0, sorted.length);
        Arrays.parallelSort(expected);
        assertArrayEquals(expected, sorted);
    }

    @Test
    public void testSortedOrder() {
        int[] sorted = CustomOrder.patialOrder(100000);
        int[] expected = new int[100000];
        System.arraycopy(sorted, 0, expected, 0, sorted.length);
        ParSort sort = new ParSort(50000, 100, false);
        sort.sort(sorted, 0, sorted.length);
        Arrays.parallelSort(expected);
        assertArrayEquals(expected, sorted);
    }




}
