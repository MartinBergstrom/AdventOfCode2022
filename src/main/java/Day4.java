import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) throws Exception {
        System.out.println(solvePart1());
    }

    private static long solvePart1() throws Exception {
        return Files.lines(Paths.get(Day4.class.getResource("Day4.txt").toURI()))
                .filter(Day4::isOverlapping)
                .count();
    }

    private static boolean isOverlapping(String line) {
        String sectionA = line.split(",")[0];
        int startA = Integer.parseInt(sectionA.split("-")[0]);
        int endA = Integer.parseInt(sectionA.split("-")[1]);

        String sectionB = line.split(",")[1];
        int startB = Integer.parseInt(sectionB.split("-")[0]);
        int endB = Integer.parseInt(sectionB.split("-")[1]);

        boolean isAOverlappedByB =
                IntStream.range(startA, endA).allMatch(numberA -> IntStream.range(startB, endB).anyMatch(numberB -> numberB == numberA));

        boolean isBOverlappedByA =
                IntStream.range(startB, endB).allMatch(numberB -> IntStream.range(startA, endA).anyMatch(numberA -> numberA == numberB));

        return isAOverlappedByB || isBOverlappedByA;
    }

}
