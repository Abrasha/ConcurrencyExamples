import kotlin.concurrent.thread

/**
 * @author Andrii Abramov on 1/1/17.
 */
fun startThread(block: () -> Unit): Thread {
    return thread(start = true, block = block)
}