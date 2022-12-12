import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {
    public static void main(String[] args) throws Exception {
        System.out.println(solvePart1());
        System.out.println(solvePart2());
    }

    private static String solvePart1() throws Exception {
        final List<Stack<Character>> supplyStack = initializeSupplyStack();

        genericSolve((nbrOfCrates, fromColumn, toColumn) -> {
            for (int i = 0; i < nbrOfCrates; i++) {
                Character character = supplyStack.get(fromColumn - 1).pop();
                supplyStack.get(toColumn - 1).push(character);
            }
        });

        return topCrateString(supplyStack);
    }

    private static String solvePart2() throws Exception {
        final List<Stack<Character>> supplyStack = initializeSupplyStack();

        genericSolve((nbrOfCrates, fromColumn, toColumn) -> {
            Stack<Character> fromStack = supplyStack.get(fromColumn - 1);
            List<Character> split = new ArrayList<>(fromStack.subList(fromStack.size() - nbrOfCrates, fromStack.size()));

            IntStream.range(0, nbrOfCrates).forEach(__ -> fromStack.pop());
            supplyStack.get(toColumn - 1).addAll(split);
        });

        return topCrateString(supplyStack);
    }

    private static List<Stack<Character>> initializeSupplyStack() throws Exception {
        File file = new File(Day5.class.getResource("day5.txt").toURI());

        List<Stack<Character>> finalizedStack = new ArrayList<>();

        List<String> supplyStackLines = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("move")) {
                    break;
                }
                supplyStackLines.add(line);
            }
        }
        String[] array = supplyStackLines.get(supplyStackLines.size() - 2).split(" ");
        int columns = Integer.parseInt(array[array.length - 1]);

        supplyStackLines.remove(supplyStackLines.size() - 1);
        supplyStackLines.remove(supplyStackLines.size() - 1);

        Collections.reverse(supplyStackLines);

        Stream.generate(Stack<Character>::new).limit(columns).forEach(finalizedStack::add);

        for (String lastLine : supplyStackLines) {
            addToStackFromLine(finalizedStack, lastLine);
        }

        return finalizedStack;
    }

    private static void addToStackFromLine(List<Stack<Character>> finalizedStack, String line) {
        char[] chars = line.toCharArray();
        int index = 0;
        for (int i = 1; i < line.length(); i++) {
            char currentChar = chars[i];
            if (currentChar != ' ') {
                finalizedStack.get(index).push(currentChar);
            }
            i = i + 3;
            index++;
        }
    }

    private static String topCrateString(List<Stack<Character>> supplyStack) {
        return supplyStack.stream()
                .map(Stack::peek)
                .reduce("", (a, b) -> a + b.toString(), (a, b) -> a + b);
    }


    private static void genericSolve(CraneLiftFunction craneLiftFn) throws Exception {
        Files.lines(Paths.get(Day5.class.getResource("day5.txt").toURI()))
                .filter(line -> line.contains("move"))
                .forEach(instruction -> {
                    instruction = instruction.replaceAll("[^0-9]+", " ");
                    String[] numbers = instruction.trim().split(" ");

                    int nbrOfCrates = Integer.parseInt(numbers[0]);
                    int fromColumn = Integer.parseInt(numbers[1]);
                    int toColumn = Integer.parseInt(numbers[2]);

                    craneLiftFn.apply(nbrOfCrates, fromColumn, toColumn);
                });
    }

    private interface CraneLiftFunction {
        void apply(int nbrOfCrates, int fromColumn, int toColumn);
    }

}
