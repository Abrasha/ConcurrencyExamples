import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

/**
 * @author Andrii Abramov on 1/1/17.
 */
class ClientMessenger(val clientSocket: ClientSocket) {

    val messageQueue: BlockingQueue<Message> = ArrayBlockingQueue(10)
    var connectionPresent = false

    init {
        Thread({ handleSendingMessages() }).start()
    }

    private fun handleSendingMessages() {
        while (connectionPresent) {
            try {
                messageQueue.take()

            } catch (e: InterruptedException) {
                // connection closed
                break
            }
        }
    }

    fun sendMessage(message: Message) {
        messageQueue.add(message)
    }

}