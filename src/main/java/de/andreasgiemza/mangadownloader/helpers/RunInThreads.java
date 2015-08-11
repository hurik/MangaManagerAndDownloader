package de.andreasgiemza.mangadownloader.helpers;

import java.util.List;
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
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (final Runnable runnable : runnables) {
            executor.execute(runnable);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }
}
