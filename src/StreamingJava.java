
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamingJava {

    // Aufgabe 2) a)
    public static <E> Stream<E> flatStreamOf(List<List<E>> list) {
        return list.stream().flatMap(List::stream);
    }

    // Aufgabe 2) b)
    public static int bitsOf(IntStream stream) {
        // TODO‚
        return 0;
    }

    // Aufgabe 2) c)
    public static <E extends Comparable<? super E>> E minOf(List<List<E>> list) {
        // TODO
        return null;
    }

    // Aufgabe 2) d)
    public static <E> E lastWithOf(Stream<E> stream, Predicate<? super E> predicate) {
        // TODO
        return null;
    }

    // Aufgabe 2) e)
    public static <E> Set<E> findOfCount(Stream<E> stream, int count) {
        // TODO
        return null;
    }

    // Aufgabe 2) f)
    public static IntStream makeStreamOf(String[] strings) {
        // TODO
        return null;
    }

//-------------------------------------------------------------------------------------------------
    // Aufgabe 3) a)
    public static Stream<String> fileLines(String path) throws IOException {
        // TODO
        return null;
    }

    // Aufgabe 3) b)
    public static double averageCost(Stream<String> lines) {
        // TODO
        return 0d;
    }

    // Aufgabe 3) c)
    public static long countCleanEnergyLevy(Stream<String> lines) {
        // TODO
        return 0L;
    }

    // Aufgabe 3) d)
    // TODO
    // Aufgabe 3) e)
    // TODO
    // Aufgabe 3) f)
    // TODO
    // Aufgabe 3) g)
    // TODO
    // Aufgabe 3) h)
    public static Stream<File> findFilesWith(String dir, String st, String ed, int maxFiles) throws IOException {
        // TODO
        return null;
    }

    public static void main(String[] args) throws Exception {
        // TODO
        List<List<Integer>> list = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        System.out.println(flatStreamOf(list).toList());
    }
}
