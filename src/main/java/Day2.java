import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


// A = ROCK
// B = PAPER
// C = SCISSOR

// PART 1
// X = ROCK
// Y = PAPER
// Z = SCISSOR

// PART 2
// X = LOOSE
// Y = DRAW
// Z = WIN



public class Day2 {

    private static final Map<String, Integer> PLAYER_B = Map.of(
            "X", 1,
            "Y", 2,
            "Z", 3);


    public static void main(String[] args) throws Exception {
        // Part 1
        System.out.println(Files.lines(Paths.get(Day2.class.getResource("day2.txt").toURI()))
                .map(line -> calculate(convertChar(line.split(" ")[0]), convertChar(line.split(" ")[1])))
                .reduce(0, Integer::sum));

        // Part 2
        System.out.println(Files.lines(Paths.get(Day2.class.getResource("day2.txt").toURI()))
                .map(Day2::convertLineForPart2)
                .map(line -> calculate(convertChar(line.split(" ")[0]), convertChar(line.split(" ")[1])))
                .reduce(0, Integer::sum));
    }

    private static String convertLineForPart2(String line) {
        int choiceA = convertChar(line.split(" ")[0]);
        int choiceB = convertChar(line.split(" ")[1]);
        if (choiceB == 2) {
            return line.substring(0, line.length() - 1)  + convertNumber(choiceA);
        }
        if (choiceB == 3) {
            int newChoiceB = choiceB;
            switch (choiceA) {
                case 1:
                    newChoiceB = 2;
                    break;
                case 2:
                    newChoiceB = 3;
                    break;
                case 3:
                    newChoiceB = 1;
            }
            return line.substring(0, line.length() - 1 ) + convertNumber(newChoiceB);
        }
        int newChoiceB = choiceB;
        switch (choiceA) {
            case 1:
                newChoiceB = 3;
                break;
            case 2:
                newChoiceB = 1;
                break;
            case 3:
                newChoiceB = 2;
        }
        return line.substring(0, line.length() - 1) + convertNumber(newChoiceB);
    }

    private static int calculate(int scoreA, int scoreB) {
        if (scoreA == scoreB)
            return scoreB + 3;
        else if ((scoreA == 1 && scoreB == 2) ||
                (scoreA == 2 && scoreB == 3) ||
                (scoreA == 3 && scoreB == 1))
            return scoreB + 6;
        else
            return scoreB;
    }

    private static int convertChar(String character) {
        switch (character) {
            case "A":
            case "X":
                return 1;
            case "B":
            case "Y":
                return 2;
            case "C":
            case "Z":
                return 3;
        }
        return 0;
    }

    private static String convertNumber(int number)
    {
        switch (number) {
            case 1:
                return "X";
            case 2:
                return "Y";
            case 3:
                return "Z";
        }
        return "";
    }

}
