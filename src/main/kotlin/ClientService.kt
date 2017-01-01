/**
 * @author Andrii Abramov on 1/1/17.
 */
class ClientService<T> {

    val clients = mutableSetOf<T>()

    fun addClient(client: T) = clients.add(client)
    fun removeClient(client: T) = clients.remove(client)

}