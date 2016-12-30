import java.net.Socket

data class Client(val clientSocket: Socket, val clientName: String)
data class Message(val from: Client, val text: String)