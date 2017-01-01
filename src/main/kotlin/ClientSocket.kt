import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class ClientSocket(val address: InetAddress, val port: Int) {
    val threadPool: ExecutorService = Executors.newCachedThreadPool()
    val socket = Socket()

    fun connectToServer() {
        socket.connect(InetSocketAddress(address, port))

        // Reading from server in separate thread
        val readingServerThread = thread(start = true) { readServer(socket) }

        startWritingThread(readingServerThread)
    }

    private fun startWritingThread(readingServerThread: Thread) {
        val outputStream = SocketWriter(socket.outputStream)

        val scanner = Scanner(System.`in`)

        println("What is your name?")
        val clientName = scanner.nextLine()
        sendMessage(clientName, outputStream)

        outputStream.write(clientName)

        while (!socket.isClosed) {
            val messageToServer = scanner.nextLine()
            when (messageToServer?.toLowerCase()) {
                "exit" -> {
                    readingServerThread.interrupt()
                    socket.close()
                }
                else -> sendMessage(messageToServer, outputStream)
            }
        }
    }

    private fun sendMessage(messageToServer: String, outputStream: SocketWriter) {
        threadPool.execute { sendMessageToServer(messageToServer, outputStream) }
    }

    private fun sendMessageToServer(messageToServer: String, outputStream: SocketWriter) {
        outputStream.write(messageToServer)
    }

    fun readServer(socket: Socket) {
        val serverReader = SocketReader(socket.inputStream)
        while (!socket.isClosed) {
            try {
                val messageFromServer = serverReader.readLine()
                println(messageFromServer)
            } catch (e: InterruptedException) {
                // properly close the connection after interrupting from writing thread
                break
            }

        }
    }

}