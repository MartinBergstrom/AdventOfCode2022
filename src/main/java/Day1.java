import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class Day1 {

    public static void main(String[] args) throws Exception {
        List<List<String>> elfWithFoods = Files.lines(Paths.get(Day1.class.getResource("day1.txt").toURI()))
                .collect(Collector.of(ArrayList::new,
                        (accumulator, item) -> {
                            if (accumulator.isEmpty() || item.isEmpty()) {
                                accumulator.add(new ArrayList<>());
                            } else {
                                accumulator.get(accumulator.size() - 1).add(item);
                            }
                        }, (li1, li2) -> li2));

        System.out.println(elfWithFoods.stream()
                .map(elf -> elf.stream()
                        .map(Integer::parseInt)
                        .reduce(0, Integer::sum)).
                        min(Collections.reverseOrder())
                .get());
    }

}
