package ua.aabramov.concurrency;

import ua.aabramov.concurrency.util.StressTestExecutor;

/**
 * Created by Andrii Abramov on 1/4/17.
 */
public class ThreadSynchronizedInc {

    private static class State {
        private volatile int number = 0;

        private void inc() {
            number++;
        }

    }

    public static void main(String[] args) {
        State state = new State();
        int times = 10_000;
        StressTestExecutor.executeUnderStress(state::inc, times);

        System.out.println("Expected = " + times);
        System.out.println("Actual   = " + state.number);
    }

}
