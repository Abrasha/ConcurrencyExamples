package ua.aabramov.concurrency.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrii Abramov on 1/4/17.
 */
public class StressTestExecutor implements AutoCloseable {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void executeUnderStress(Runnable block, int times) {
        try (StressTestExecutor executor = new StressTestExecutor()) {
            CountDownLatch countDownLatch = new CountDownLatch(times);
            for (int i = 0; i < times; i++) {
                executor.executorService.execute(() -> {
                    try {
                        block.run();
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }

            int await = times / 100;
            countDownLatch.await(await > 10 ? await : 10, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception: ", e);
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }

}
