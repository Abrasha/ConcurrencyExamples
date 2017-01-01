import ServerConfig.DEFAULT_SERVER_PORT
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Client {

    private val executor: ExecutorService = Executors.newCachedThreadPool()

    fun connectToServer(address: InetAddress = InetAddress.getByName("localhost"), port: Int = DEFAULT_SERVER_PORT) {
        val socket = Socket(address, port)

        // Reading from server in separate thread
        val listeningThread = startListeningServer(socket)

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
                    listeningThread.interrupt()
                    socket.close()
                }
                else -> sendMessage(messageToServer, outputStream)
            }
        }

        executor.shutdown()
    }

    private fun startListeningServer(socket: Socket): Thread {
        return startThread { readServer(socket) }
    }

    private fun sendMessage(messageToServer: String, outputStream: SocketWriter) {
        executor.execute { sendMessageToServer(messageToServer, outputStream) }
    }

    fun sendMessageToServer(messageToServer: String, outputStream: SocketWriter) {
        outputStream.write(messageToServer)
    }

    fun readServer(socket: Socket) {
        val serverReader = SocketReader(socket.inputStream)
        while (!socket.isClosed) {
            try {
                val messageFromServer = serverReader.readLine()
                println(messageFromServer)
            } catch (e: SocketException) {
                if (Thread.interrupted()) {
                    println("Connection closed properly")
                    break
                } else {
                    println("Unexpected connection close")
                    println(e.message)
                }
            }
        }
    }
}