package ua.aabramov.concurrency;

import ua.aabramov.concurrency.util.StressTestExecutor;

/**
 *
 * @see <a href="http://stackoverflow.com/questions/25425130/loop-doesnt-see-changed-value-without-a-print-statement>Loop doesn't see changed value without a print statement</a>
 *
 * Created by Andrii Abramov on 1/4/17.
 */
public class ThreadSafeInc {

    private static class State {
        private int number = 0;

        private synchronized void inc() {
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
