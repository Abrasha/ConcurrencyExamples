import java.io.EOFException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val DEFAULT_SERVER_PORT = 25555

val clientService = ClientService<Client>()
private val executor: ExecutorService = Executors.newCachedThreadPool()

fun main(args: Array<String>) {
    val server = ServerSocket(DEFAULT_SERVER_PORT)
    while (true) {
        println("Waiting for client ...")
        val client = server.accept()
        startThread { handleClient(client) }
    }
    executor.shutdown()
}

private fun handleClient(client: Socket) {
    println("Client connected: ${client.inetAddress}")
    val inputStream = SocketReader(client.inputStream)

    val clientName = inputStream.readLine()
    val clientClass = Client(clientSocket = client, clientName = clientName)
    clientService.addClient(clientClass)

    sendMessage(Message(clientClass, "$clientName joined this chat"))

    while (!client.isClosed) {
        try {
            val message = inputStream.readLine()
            sendMessage(Message(clientClass, message))
        } catch (e: EOFException) {
            break
        }
    }

    clientService.removeClient(clientClass)

    sendMessage(Message(clientClass, "$clientName left this chat"))

}

private fun sendMessage(message: Message) {
    clientService.clients.forEach {
        executor.execute {
            sendMessageToClient(SocketWriter(it.clientSocket.outputStream), message)
        }
    }
}

private fun sendMessageToClient(outputStream: SocketWriter, message: Message) {
    outputStream.write("${message.from.clientName}: ${message.text}")
    outputStream.flush()
}