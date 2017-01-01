import ClientInputListener.OnMessageReceivedListener
import java.io.EOFException
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

val DEFAULT_SERVER_PORT = 25555

val clientService = ClientService()

fun main(args: Array<String>) {
    val server = ServerSocket(DEFAULT_SERVER_PORT)
    while (true) {
        println("Waiting for clientSocket ...")
        acceptClient(server.accept())
    }
}

private fun acceptClient(client: Socket) {
    thread(start = true) { handleClient(client) }
}

private fun handleClient(client: Socket) {
    println("ClientSocket connected: ${client.inetAddress}")

    val clientMessageHandler = ClientInputListener(client)

    val inputStream = SocketReader(client.inputStream)

//    val clientName = inputStream.readLine()
    val clientName = clientMessageHandler.getMessage()
    // 'handshake' done
    val clientClass = ClientWrapper(client, clientName)

    clientService.addClient(clientClass)
    clientService.sendMessage(Message(clientClass, "$clientName joined this chat"))

    while (!client.isClosed) {
        try {
            val message = inputStream.readLine()
            clientService.sendMessage(Message(clientClass, message))
        } catch (e: EOFException) {
            break
        }
    }

    clientService.removeClient(clientClass)
    clientService.sendMessage(Message(clientClass, "$clientName left this chat"))
}