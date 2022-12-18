import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) throws Exception {
        solvePart1(); //1428881
        solvePart2(); //10475598
    }

    private static void solvePart1() throws Exception {
        System.out.println(calculateNumberOfDirectoriesWithSizeUnder100000(createDirectoryStructure()));
    }

    private static void solvePart2() throws Exception {
        int totalDiscSpace = 70_000_000;
        int needSpace = 30_000_000;

        Directory rootDir = createDirectoryStructure();
        int needToFreeUp = needSpace - (totalDiscSpace - ((int) rootDir.calculateTotalSize()));

        Map<String, Long> dirAndTotalSize = new HashMap<>();
        calculateDirAndTotalSizes(rootDir, dirAndTotalSize);
        Map<String, Long> sorted = dirAndTotalSize.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println(sorted.values().stream().filter(size -> size >= (long) needToFreeUp).findFirst().get());
    }

    private static void calculateDirAndTotalSizes(Directory dir, Map<String, Long> resultMap) {
        if (!dir.childrenDirectories.isEmpty()) {
            for (Directory childDir : dir.childrenDirectories) {
                calculateDirAndTotalSizes(childDir, resultMap);
            }
        }
        resultMap.put(dir.name, dir.calculateTotalSize());
    }

    private static Directory createDirectoryStructure() throws Exception {
        File file = new File(Day7.class.getResource("day7.txt").toURI());
        Directory rootDirectory;
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            Directory currentDir = new Directory("/");
            rootDirectory = currentDir;
            LinkedList<Directory> previousDirs = new LinkedList<>();
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("$")) {
                    String command = line.split(" ")[1];
                    if (command.equals("cd")) {
                        String moveTo = line.split(" ")[2];
                        if (moveTo.equals("..")) {
                            // move back to previous directory
                            currentDir = previousDirs.removeLast();

                        } else {
                            // Move to child dir
                            previousDirs.addLast(currentDir);
                            currentDir = currentDir.getChildDirectory(moveTo);
                        }
                    }
                } else {
                    if (line.startsWith("dir")) {
                        handleChildDirectory(currentDir, line);
                    } else {
                        addChildFile(currentDir, line);
                    }
                }
            }
        }
        return rootDirectory;
    }


    private static long calculateNumberOfDirectoriesWithSizeUnder100000(Directory dir) {
        long totalSize = 0L;
        if (!dir.childrenDirectories.isEmpty()) {
            for (Directory childDir : dir.childrenDirectories) {
                totalSize += calculateNumberOfDirectoriesWithSizeUnder100000(childDir);
            }
        }
        long totalSizeOfDir = dir.calculateTotalSize();
        if (totalSizeOfDir <= 100_000L) {
            totalSize += totalSizeOfDir;
        }

        return totalSize;
    }

    private static void handleChildDirectory(Directory currentDir, String line) {
        String directoryName = line.split(" ")[1];
        if (!currentDir.containsDirectory(directoryName)) {
            Directory newChildDir = new Directory(directoryName);
            currentDir.addChildDirectory(newChildDir);
        }
    }

    private static void addChildFile(Directory currentDir, String line) {
        String fileName = line.split(" ")[1];
        long fileSize = Long.valueOf(line.split(" ")[0]);
        currentDir.addFile(fileName, fileSize);
    }

    private static class Directory {
        private String name;
        private Map<String, Long> filesWithSizes = new HashMap<>();
        private List<Directory> childrenDirectories = new ArrayList<>();
        private long mySize = 0L;

        private Directory(String name) {
            this.name = name;
        }

        private void addFile(String name, long size) {
            if (filesWithSizes.get(name) != null) {
                throw new RuntimeException();
            }
            filesWithSizes.put(name, size);
            this.mySize += size;
        }

        private long calculateTotalSize() {
            long totalSizeIncludingChildDirs = this.mySize;
            for (Directory child : childrenDirectories) {
                totalSizeIncludingChildDirs += child.calculateTotalSize();
            }
            return totalSizeIncludingChildDirs;
        }

        private boolean containsDirectory(String directoryName) {
            return this.childrenDirectories.stream().anyMatch(directory -> directory.name.equals(directoryName));
        }

        private void addChildDirectory(Directory childDirectory) {
            this.childrenDirectories.add(childDirectory);
        }

        private Directory getChildDirectory(String directoryName) {
            return this.childrenDirectories.stream()
                    .filter(childDir -> childDir.name.equals(directoryName))
                    .findFirst()
                    .orElseThrow();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31).
                    append(name).
                    append(filesWithSizes).
                    append(childrenDirectories).
                    append(mySize).
                    toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Directory))
                return false;
            if (obj == this)
                return true;

            Directory other = (Directory) obj;
            return new EqualsBuilder().
                    append(name, other.name).
                    append(filesWithSizes, other.filesWithSizes).
                    append(childrenDirectories, other.childrenDirectories).
                    append(mySize, other.mySize).
                    isEquals();
        }


    }
}
