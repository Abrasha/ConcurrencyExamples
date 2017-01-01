import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Andrii Abramov on 1/1/17.
 */
class ClientService {

    val threadPool: ExecutorService = Executors.newCachedThreadPool()
    val clients = mutableSetOf<ClientWrapper>()

    fun addClient(client: ClientWrapper) {
        clients.add(client)
    }

    fun removeClient(client: ClientWrapper) {
        clients.remove(client)
    }

    fun sendMessage(message: Message) {
        clients.forEach { client ->
            threadPool.execute {
                sendMessageToClient(SocketWriter(client.socket.outputStream), message)
            }
        }
    }

    private fun sendMessageToClient(outputStream: SocketWriter, message: Message) {
        outputStream.write("${message.from.clientName}: ${message.text}")
        outputStream.flush()
    }

}