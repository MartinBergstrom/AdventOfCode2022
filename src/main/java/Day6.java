import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void main(String[] args) throws Exception {
        solvePart1();
        solvePart2();
    }

    private static void solvePart1() throws Exception {
        solve(4);
    }

    private static void solvePart2() throws Exception {
        solve(14);
    }

    private static void solve(int limit) throws Exception {
        File file = new File(Day6.class.getResource("day6.txt").toURI());
        List<Character> fourChars = new ArrayList<>();
        int count = 0;
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            int c;
            while ((c = br.read()) != -1) {
                char character = (char) c;
                count++;
                if (fourChars.size() == limit) {
                    fourChars.remove(0);
                    fourChars.add(character);
                    if (fourChars.stream().distinct().count() == limit) {
                        System.out.println(count);
                        break;
                    }
                } else {
                    fourChars.add(character);
                }
            }
        }
    }

}
