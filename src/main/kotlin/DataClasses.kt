import java.net.Socket

data class ClientWrapper(val socket: Socket, val clientName: String)

data class Message(val from: ClientWrapper, val text: String)