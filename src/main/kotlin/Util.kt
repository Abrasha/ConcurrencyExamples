import kotlin.concurrent.thread

fun startThread(block: () -> Unit): Thread {
    return thread(start = true, block = block)
}