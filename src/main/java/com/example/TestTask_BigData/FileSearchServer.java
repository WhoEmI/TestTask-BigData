package com.example.TestTask_BigData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;

public class FileSearchServer {
    private static final ArrayDeque<PrintWriter> clients = new ArrayDeque<>();
    private static final Object lock = new Object();

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java FileSearchServer serverPort rootPath");
            return;
        }

        int serverPort = Integer.parseInt(args[0]);
        String rootPath = args[1];

        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server started on port " + serverPort);

        // Search thread
        Thread searchThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (clients.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (PrintWriter client : clients) {
                        search(rootPath, client);
                    }
                    clients.clear();
                }
            }
        });
        searchThread.start();

        // Accept client connections
        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            synchronized (lock) {
                clients.add(out);
                lock.notify();
            }
        }
    }

    private static void search(String rootPath, PrintWriter out) {
        File rootDir = new File(rootPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            out.println("Invalid root directory");
            return;
        }

        ArrayDeque<File> queue = new ArrayDeque<>();
        queue.add(rootDir);

        while (!queue.isEmpty()) {
            File current = queue.poll();
            if (current.isDirectory()) {
                for (File file : current.listFiles()) {
                    if (file.isDirectory()) {
                        queue.add(file);
                    } else {
                        out.println(file.getAbsolutePath());
                    }
                }
            }
        }
        out.println("Search complete.");
    }
}
