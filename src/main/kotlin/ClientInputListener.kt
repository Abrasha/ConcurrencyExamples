import java.net.Socket

/**
 * @author Andrii Abramov on 1/1/17.
 */
class ClientInputListener(val clientSocket: Socket) {

    val reader = SocketReader(clientSocket.inputStream)

    interface OnMessageReceivedListener {
        fun onMessageReceived(message: Message)
    }

    fun getMessage(): String {
        return readMessage()
    }

    private fun readMessage(): String {
        return reader.readLine()
    }

    fun startListening(listener: OnMessageReceivedListener) {

    }


}