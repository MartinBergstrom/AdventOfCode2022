import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) throws Exception {
        System.out.println(solvePart1());
        System.out.println(solvePart2());
    }

    private static long solvePart1() throws Exception {
        return Files.lines(Paths.get(Day4.class.getResource("Day4.txt").toURI()))
                .map(Day4::createPair)
                .filter(Day4::isOverlappingCompletely)
                .count();
    }

    private static long solvePart2() throws Exception {
        return Files.lines(Paths.get(Day4.class.getResource("Day4.txt").toURI()))
                .map(Day4::createPair)
                .filter(Day4::isOverlappingPartially)
                .count();
    }

    private static Pair<List<Integer>> createPair(String line) {
        String sectionA = line.split(",")[0];
        int startA = Integer.parseInt(sectionA.split("-")[0]);
        int endA = Integer.parseInt(sectionA.split("-")[1]);

        String sectionB = line.split(",")[1];
        int startB = Integer.parseInt(sectionB.split("-")[0]);
        int endB = Integer.parseInt(sectionB.split("-")[1]);

        List<Integer> rangeA = IntStream.range(startA, (endA + 1)).boxed().collect(Collectors.toList());
        List<Integer> rangeB = IntStream.range(startB, (endB + 1)).boxed().collect(Collectors.toList());
        return new Pair<>(rangeA, rangeB);
    }

    private static boolean isOverlappingCompletely(Pair<List<Integer>> pair) {
        return isRangeFullyOverlappingOtherRange(pair.first, pair.second) ||
                isRangeFullyOverlappingOtherRange(pair.second, pair.first);
    }

    private static boolean isOverlappingPartially(Pair<List<Integer>> pair) {
        return isRangeOverlappingOtherRange(pair.first, pair.second) ||
                isRangeOverlappingOtherRange(pair.second, pair.first);
    }

    private static boolean isRangeFullyOverlappingOtherRange(List<Integer> rangeA, List<Integer> rangeB) {
        return rangeA.stream().allMatch(numberA -> rangeB.stream().anyMatch(numberB -> numberB.equals(numberA)));
    }

    private static boolean isRangeOverlappingOtherRange(List<Integer> rangeA, List<Integer> rangeB) {
        return rangeA.stream().anyMatch(numberA -> rangeB.stream().anyMatch(numberB -> numberB.equals(numberA)));
    }

    private static class Pair<T> {
        T first;
        T second;

        Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }
    }

}
