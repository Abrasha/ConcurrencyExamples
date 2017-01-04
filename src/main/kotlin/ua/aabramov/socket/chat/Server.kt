package ua.aabramov.socket.chat

import ua.aabramov.socket.chat.ServerConfig.DEFAULT_SERVER_PORT
import java.io.EOFException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Andrii Abramov on 1/1/17.
 */
class Server {

    private val clientService = ClientService<ClientWrapper>()
    private val executor: ExecutorService = Executors.newCachedThreadPool()

    fun startListening(port: Int = DEFAULT_SERVER_PORT) {
        val server = ServerSocket(port)
        while (!server.isClosed) {
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
        val clientClass = ClientWrapper(clientSocket = client, clientName = clientName)
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
        val latch = CountDownLatch(clientService.clients.size)
        clientService.clients.forEach {
            executor.execute {
                try {
                    sendMessageToClient(SocketWriter(it.clientSocket.outputStream), message)
                } finally {
                    latch.countDown()
                }
            }
        }

        // waiting while all messages will be sent to follow the order
        latch.await()
    }

    private fun sendMessageToClient(outputStream: SocketWriter, message: Message) {
        outputStream.write("${message.from.clientName}: ${message.text}")
        outputStream.flush()
    }
}