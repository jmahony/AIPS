package com.joshmahony;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by josh on 24/02/14.
 */
public class ProcessingQueue<T> {

    ConcurrentLinkedQueue<T> queue;

    public ProcessingQueue() {

        queue = new ConcurrentLinkedQueue<T>();

        populateQueue();

    }

    public T poll() {

        return queue.poll();

    }

    private void populateQueue() {

        File directory = new File("src/main/resources/documents/");

        if (!directory.exists()) {
            System.out.println("Dir not found: " + directory.getAbsolutePath());
            return;
        }

        FileReader fileReader;

        BufferedReader bufferedReader;

        for (File file : directory.listFiles()) {

            try {

                fileReader = new FileReader(file);

                bufferedReader = new BufferedReader(fileReader);

                String s;

                StringBuffer sb = new StringBuffer();

                while((s = bufferedReader.readLine()) != null) {

                    sb.append(s + "\n");

                }

                queue.add((T) sb.toString());

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        System.out.println("Done!");

    }



}
