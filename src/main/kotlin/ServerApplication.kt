import java.io.EOFException
import java.net.ServerSocket
import java.net.Socket

val DEFAULT_SERVER_PORT = 25555

val clients = mutableListOf<Client>()

fun main(args: Array<String>) {
    val server = ServerSocket(DEFAULT_SERVER_PORT)
    while (true) {
        println("Waiting for client ...")
        val client = server.accept()
        acceptClient(client)
    }
}

private fun acceptClient(client: Socket) {
    Thread({ handleClient(client) }).start()
}

private fun handleClient(client: Socket) {
    println("Client connected: ${client.inetAddress}")
    val inputStream = SocketReader(client.inputStream)

    val clientName = inputStream.readLine()
    val clientClass = Client(clientSocket = client, clientName = clientName)
    clients.add(clientClass)

    sendMessage(Message(clientClass, "$clientName joined this chat"))

    while (!client.isClosed) {
        try {
            val message = inputStream.readLine()
            sendMessage(Message(clientClass, message))
        } catch (e: EOFException) {
            break
        }
    }

    clients.remove(clientClass)

    sendMessage(Message(clientClass, "$clientName left this chat"))

}

private fun sendMessage(message: Message) {
    clients.forEach { sendMessageToClient(SocketWriter(it.clientSocket.outputStream), message) }
}

private fun sendMessageToClient(outputStream: SocketWriter, message: Message) {
    outputStream.write("${message.from.clientName}: ${message.text}")
    outputStream.flush()
}