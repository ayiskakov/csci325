import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FileRenamer {

    public static void main(String[] args) {
        String directoryPath = "./files"; 
        File dir = new File(directoryPath);

        if (!dir.isDirectory()) {
            System.out.println("Provided path is not a directory.");
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the directory.");
            return;
        }

        int coreCount = Runtime.getRuntime().availableProcessors();

        System.out.println("Number of threads: " + coreCount);
        
        ExecutorService executor = Executors.newFixedThreadPool(coreCount);
        
        AtomicInteger fileCounter = new AtomicInteger(1);

        for (File file : files) {
            executor.execute(new FileRenameTask(file, fileCounter, directoryPath));
        }

        executor.shutdown();
    }

    static class FileRenameTask implements Runnable {
        private final File file;
        private final AtomicInteger fileCounter;
        private final String directoryPath;

        public FileRenameTask(File file, AtomicInteger fileCounter, String directoryPath) {
            this.file = file;
            this.fileCounter = fileCounter;
            this.directoryPath = directoryPath;
        }

        @Override
        public void run() {
            String fileExtension = getFileExtension(file);
            String newFileName = directoryPath + File.separator + fileCounter.getAndIncrement() + fileExtension;
            File newFile = new File(newFileName);

            if (!newFile.exists()) {
                boolean renamed = file.renameTo(newFile);
                if (renamed) {
                    System.out.println("Renamed " + file.getName() + " to " + newFile.getName());
                } else {
                    System.out.println("Failed to rename " + file.getName());
                }
            }
        }

        private String getFileExtension(File file) {
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf == -1) {
                return ""; // No extension found
            }
            return name.substring(lastIndexOf);
        }
    }
}
