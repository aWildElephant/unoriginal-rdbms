package fr.awildelephant.csvloader;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

final class ProgressLogger {

    private static final Logger LOGGER = LogManager.getLogger("CSV Loader");
    private static final int LOG_THRESHOLD = 100_000;

    private int counter = 0;
    private StopWatch stopWatch;

    void start() {
        stopWatch = StopWatch.createStarted();
    }

    void log(int numberOfRowsInserted) {
        counter += numberOfRowsInserted;

        if (counter >= LOG_THRESHOLD) {
            LOGGER.info("Inserted {} rows in {}", counter, stopWatch);
            counter = 0;
            stopWatch.reset();
            stopWatch.start();
        }
    }
}
