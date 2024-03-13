package com.example.TestTask_BigData;

import java.io.File;
import java.util.ArrayDeque;

public class FileSearchAsync {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java FileSearchAsync rootPath depth mask");
            return;
        }

        String rootPath = args[0];
        final int[] depth = {Integer.parseInt(args[1])};
        String mask = args[2];

        ArrayDeque<File> queue = new ArrayDeque<>();
        queue.add(new File(rootPath));

        Thread searchThread = new Thread(() -> {
            while (!queue.isEmpty() && depth[0] >= 0) {
                int size = queue.size();
                while (size-- > 0) {
                    File current = queue.poll();
                    if (current.isDirectory()) {
                        for (File file : current.listFiles()) {
                            if (file.isDirectory()) {
                                queue.add(file);
                            } else if (file.getName().contains(mask)) {
                                System.out.println(file.getAbsolutePath());
                            }
                        }
                    }
                }
                depth[0]--;
            }
        });
        searchThread.start();

        // Output thread
        Thread outputThread = new Thread(() -> {
            while (searchThread.isAlive()) {
                // Do nothing until search is completed
            }
            System.out.println("Search complete.");
        });
        outputThread.start();
    }
}
