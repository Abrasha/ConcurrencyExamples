import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Andrii Abramov on 12/30/16.
 */
class SocketReader(reader: InputStream) {

    val inputStream = DataInputStream(reader)

    fun readLine(): String = inputStream.readUTF()
}

class SocketWriter(writer: OutputStream) {

    val outputStream = DataOutputStream(writer)

    fun write(s: String) {
        outputStream.writeUTF(s)
        flush()
    }

    fun flush() = outputStream.flush()
}
