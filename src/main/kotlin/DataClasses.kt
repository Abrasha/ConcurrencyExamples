import java.net.Socket

data class ClientWrapper(val clientSocket: Socket, val clientName: String)
data class Message(val from: ClientWrapper, val text: String)