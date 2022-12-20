import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) throws Exception {
        solvePart1(); //1669
        solvePart2(); //331344
    }

    private static void solvePart1() throws Exception {
        final Tree[][] matrix = Files.lines(Paths.get(Day8.class.getResource("day8.txt").toURI()))
                .map(line -> line.chars().map(c -> c - '0').boxed().map(Tree::new).toArray(Tree[]::new))
                .toArray(Tree[][]::new);

        int visibleHorizontally = visibleHorizontally(matrix);
        int visibleVertically = visibleVertically(matrix);
        System.out.println(visibleHorizontally + visibleVertically);
    }

    private static void solvePart2() throws Exception {
        final Tree[][] matrix = Files.lines(Paths.get(Day8.class.getResource("day8.txt").toURI()))
                .map(line -> line.chars().map(c -> c - '0').boxed().map(Tree::new).toArray(Tree[]::new))
                .toArray(Tree[][]::new);

        for (int i = 1; i < matrix.length - 1; i++) {
            for (int j = 1; j < matrix[0].length - 1; j++) {
                setScoreForTree(i, j, matrix);
            }
        }

        int maxScenicScore = Arrays.stream(matrix).flatMap(Arrays::stream).mapToInt(tree -> tree.scenicScore).max().getAsInt();
        System.out.println(maxScenicScore);
    }

    private static void setScoreForTree(int i, int j, Tree[][] matrix) {
        Tree currentTree = matrix[i][j];
        List<Integer> row = Arrays.stream(matrix[i]).map(tree -> tree.number).collect(Collectors.toList());

        int newScenicScore = calculateForward(j, currentTree, row);
        newScenicScore *= calculateBackwards(j, currentTree, row);

        List<Integer> column = Arrays.stream(matrix).map(x -> x[j]).map(tree -> tree.number).collect(Collectors.toList());
        newScenicScore *= calculateForward(i, currentTree, column);
        newScenicScore *= calculateBackwards(i, currentTree, column);

        currentTree.scenicScore = newScenicScore;
    }

    private static int calculateBackwards(int i, Tree currentTree, List<Integer> row) {
        int newScenicScore = 0;
        int index = i - 1;
        while (index >= 0) {
            int treeBackwards = row.get(index);
            if (treeBackwards < currentTree.number) {
                newScenicScore++;
                index--;
            } else {
                newScenicScore++;
                break;
            }
        }
        return newScenicScore;
    }

    private static int calculateForward(int i, Tree currentTree, List<Integer> row) {
        int newScenicScore = 0;
        int index = i + 1;
        while (index < row.size()) {
            int treeForward = row.get(index);
            if (treeForward < currentTree.number) {
                newScenicScore++;
                index++;
            } else {
                newScenicScore++;
                break;
            }
        }
        return newScenicScore;
    }

    private static int visibleVertically(Tree[][] matrix) {
        int horizontalLength = matrix[0].length;
        int count = 0;
        int highest = -1;

        // Up to down
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < horizontalLength; j++) {
                Tree tree = matrix[j][i];
                if (tree.number > highest) {
                    highest = tree.number;
                    if (!tree.counted) {
                        count++;
                        tree.counted = true;
                    }
                }
            }
            highest = -1;
        }

        highest = -1;
        // Down to up
        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int j = horizontalLength - 1; j >= 0; j--) {
                Tree tree = matrix[j][i];
                if (tree.number > highest) {
                    highest = tree.number;
                    if (!tree.counted) {
                        count++;
                        tree.counted = true;
                    }
                }
            }
            highest = -1;
        }


        return count;
    }

    private static int visibleHorizontally(Tree[][] matrix) {
        int count = 0;

        for (Tree[] row : matrix) {
            // left to right
            int currentMaxHeight = -1;
            for (int i = 0; i < row.length; i++) {
                Tree currentTree = row[i];
                if (currentTree.number > currentMaxHeight) {
                    currentMaxHeight = currentTree.number;
                    if (!currentTree.counted) {
                        count++;
                        currentTree.counted = true;
                    }
                }
            }

            // Right to left
            currentMaxHeight = -1;
            for (int i = row.length - 1; i >= 0; i--) {
                Tree currentTree = row[i];
                if (currentTree.number > currentMaxHeight) {
                    currentMaxHeight = currentTree.number;
                    if (!currentTree.counted) {
                        count++;
                        currentTree.counted = true;
                    }
                }
            }
        }
        return count;
    }

    private static class Tree {
        private int number;
        private boolean counted = false;
        private int scenicScore = 0;

        Tree(int number) {
            this.number = number;
        }
    }
}
