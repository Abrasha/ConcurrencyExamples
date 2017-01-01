import com.github.javafaker.Faker
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val queue = ArrayBlockingQueue<String>(3)

    val queueThread = Thread({
        while (true) {
            try {
                println(queue.take())
            } catch (e: InterruptedException) {
                println("interrupted")
                break
            }
        }
    })
    queueThread.start()

    val executorService = Executors.newScheduledThreadPool(3)

    repeat(10, {
        executorService.scheduleWithFixedDelay(
                { queue.add(Faker().name().firstName()) }, 0, Random().nextInt(8).toLong() + 1, TimeUnit.SECONDS
        )
    })

    executorService.scheduleWithFixedDelay(
            {
                println("Scheduled interrupt")
                queueThread.interrupt()
                executorService.shutdown()
            },
            0,
            10, TimeUnit.SECONDS
    )


}