import java.net.InetAddress

fun main(args: Array<String>) {
    val address = InetAddress.getByName("localhost")
    val clientSocket = ClientSocket(address, DEFAULT_SERVER_PORT)
    clientSocket.connectToServer()
}




