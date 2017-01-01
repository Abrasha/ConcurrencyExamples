import java.net.ServerSocket

class ServerHandler(serverSocket: ServerSocket) {
    val clients = mutableListOf<ClientWrapper>()
}