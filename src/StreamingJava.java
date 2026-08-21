
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamingJava {

    // Aufgabe 2) a)
    public static <E> Stream<E> flatStreamOf(List<List<E>> list) {
        // TODO
        return list.stream().flatMap(List::stream);
    }

    // Aufgabe 2) b)
    public static int bitsOf(IntStream stream) {
        // TODO
        return stream.reduce(0, (a, b) -> a | b);
    }

    // Aufgabe 2) c)
    public static <E extends Comparable<? super E>> E minOf(List<List<E>> list) {
        // TODO
        Stream<E> s = list.stream().flatMap(List::stream);
        Comparator c = Comparator.naturalOrder();
        Optional<E> min = s.min(c);
        return min.orElseThrow();
    }

    // Aufgabe 2) d)
    public static <E> E lastWithOf(Stream<E> stream, Predicate<? super E> predicate) {
        // TODO
        return null;
    }

    // Aufgabe 2) e)
    public static <E> Set<E> findOfCount(Stream<E> stream, int count) {
        return stream.collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
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
        // Test a
        List<List<Integer>> listA = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        System.out.println("Test a: " + flatStreamOf(listA).toList());

        // Test b
        IntStream stream = IntStream.of(1, 2, 3, 4, 5);
        IntStream streamEmpty = IntStream.of();
        System.out.println("Test b: " + bitsOf(stream));
        System.out.println("Test b empty:" + bitsOf(streamEmpty));

        // Test c
        List<List<Integer>> listC = List.of(List.of(10, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        List<List<Integer>> listCEmpty = List.of(List.of());
        System.out.println("Test c: " + minOf(listC));
        //System.out.println("Test c empty: " + minOf(listCEmpty));

        // Test e
        Stream<String> streamE = Stream.of("a", "b", "a", "c", "b", "a", "d");
        System.out.println("Test e (count=3): " + findOfCount(streamE, 3)); // Should print [b]
    }
}
