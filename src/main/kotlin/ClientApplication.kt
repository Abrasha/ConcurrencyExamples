import java.net.Socket
import java.net.SocketException
import java.util.*

fun main(args: Array<String>) {
    connectToServer()
}

fun connectToServer() {
    val socket = Socket("localhost", DEFAULT_SERVER_PORT)

    // Reading from server in separate thread
    startListeningServer(socket)

    val outputStream = SocketWriter(socket.outputStream)

    val scanner = Scanner(System.`in`)

    println("What is your name?")
    val clientName = scanner.nextLine()
    sendMessage(clientName, outputStream)

    outputStream.write(clientName)

    while (!socket.isClosed) {
        val messageToServer = scanner.nextLine()

        when (messageToServer?.toLowerCase()) {
            "exit" -> socket.close()
            else -> sendMessage(messageToServer, outputStream)
        }
    }
}

private fun startListeningServer(socket: Socket) {
    Thread({ readServer(socket) }).start()
}

private fun sendMessage(messageToServer: String, outputStream: SocketWriter) {
    Thread({ sendMessageToServer(messageToServer, outputStream) }).start()
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
            println(e.message)
        }

    }
}
