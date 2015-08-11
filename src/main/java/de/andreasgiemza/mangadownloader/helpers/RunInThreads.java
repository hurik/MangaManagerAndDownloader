package de.andreasgiemza.mangadownloader.helpers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class RunInThreads {

    private final static int numberOfThreads = 4;

    private RunInThreads() {
    }

    public static void doIt(List<Runnable> runnables) {
        List<Runnable> tempList = new LinkedList<>(runnables);
        Collections.shuffle(tempList, new Random(System.nanoTime()));

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (final Runnable runnable : tempList) {
            executor.execute(runnable);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }
}
