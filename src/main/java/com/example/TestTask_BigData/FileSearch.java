package com.example.TestTask_BigData;

import java.io.File;
import java.util.ArrayDeque;

public class FileSearch {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java FileSearch rootPath depth mask");
            return;
        }

        String rootPath = args[0];
        int depth = Integer.parseInt(args[1]);
        String mask = args[2];

        ArrayDeque<File> queue = new ArrayDeque<>();
        queue.add(new File(rootPath));

        while (!queue.isEmpty() && depth >= 0) {
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
            depth--;
        }
    }
}

