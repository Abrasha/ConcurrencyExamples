package ua.aabramov.concurrency;

/**
 * Created by Andrii Abramov on 1/4/17.
 */
public class ThreadRandomOrder {

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println(Thread.currentThread().getName())).start();
        }
    }

}
