import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {
    public static void main(String[] args) throws Exception {
        System.out.println(solvePart1());
        System.out.println(solvePart2());
    }

    private static int solvePart1() throws Exception {
        return Files.lines(Paths.get(Day3.class.getResource("Day3.txt").toURI()))
                .flatMap(line -> commonChars(line.substring(0, line.length() / 2), line.substring(line.length() / 2, line.length())))
                .map(Day3::getPrioForChar)
                .reduce(0, Integer::sum);
    }

    private static Stream<Character> commonChars(String a, String b) {
        return a.chars().mapToObj(i -> (char) i).filter(aChar -> b.indexOf(aChar) != -1).distinct();
    }

    private static int getPrioForChar(char c) {
        int score = Character.isUpperCase(c) ? 26 : 0;
        score += ("abcdefghijklmnopqrstuvwxyz".indexOf(String.valueOf(c).toLowerCase()) + 1);
        return score;
    }

    private static int solvePart2() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        return Files.lines(Paths.get(Day3.class.getResource("Day3.txt").toURI()))
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / 3))
                .values()
                .stream()
                .map(Day3::getCommonChar)
                .filter(Objects::nonNull)
                .map(Day3::getPrioForChar)
                .reduce(0, Integer::sum);
    }

    private static Character getCommonChar(List<String> chunk) {
        Iterator<String> itr = chunk.iterator();
        Set<Character> commonChars = new HashSet<>(itr.next().chars().mapToObj(i -> (char) i).collect(Collectors.toSet()));
        do {
            Set<Character> lineSet = itr.next().chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
            commonChars.retainAll(lineSet);
        } while (itr.hasNext());
        return commonChars.iterator().hasNext() ? commonChars.iterator().next() : null;
    }
}