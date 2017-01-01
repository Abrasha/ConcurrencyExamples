import java.net.Socket

/**
 * @author Andrii Abramov on 1/1/17.
 */
data class ClientWrapper(val clientSocket: Socket, val clientName: String)

/**
 * @author Andrii Abramov on 1/1/17.
 */
data class Message(val from: ClientWrapper, val text: String)