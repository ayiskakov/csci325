import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class TxtTextSearcher {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String directoryPath = args[0];
        String searchTerm = args[1];
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (listOfFiles != null) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Map<String, Integer> results = new ConcurrentHashMap<>();

            for (File file : listOfFiles) {
                executor.submit(() -> {
                    String content = readFile(file.getPath());
                    int frequency = content.toLowerCase().split(searchTerm.toLowerCase(), -1).length - 1;
                    if (frequency == 0) {
                        String[] words = searchTerm.toLowerCase().split(" ");
                        
                        Set<String> set = new HashSet<>(Arrays.asList(words));
                        set.remove("a");
                        set.remove("in");
                        set.remove("the");
                        set.remove("of");
                        set.remove("and");
                        set.remove("or");
                        set.remove("to");
                        set.remove("is");
                        set.remove("are");
                        set.remove("was");
                        set.remove("were");
                        set.remove("for");
                        set.remove("on");
                        set.remove("as");
                        set.remove("by");
                        set.remove("with");
                        words = set.toArray(new String[set.size()]);

                        for (String word : words) {
                            frequency += content.toLowerCase().split(word, -1).length - 1;
                        }
                    }
                    results.put(file.getName(), frequency);
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            // Sorting the results
            List<Map.Entry<String, Integer>> sortedResults = new ArrayList<>(results.entrySet());
            sortedResults.sort((e1, e2) -> {
                int titleComparison = Boolean.compare(e2.getKey().toLowerCase().contains(searchTerm.toLowerCase()), 
                                                     e1.getKey().toLowerCase().contains(searchTerm.toLowerCase()));
                if (titleComparison != 0) return titleComparison;
                return e2.getValue().compareTo(e1.getValue());
            });

            // Printing the results
            sortedResults.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        }
    }

    private static String readFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}