import org.junit.Test



import org.junit.Assert.*
import org.junit.Before

class communicationTest {
    @Before
    fun fill() {
        communication.clear()
    }

    @Test
    fun createChatTest() {
        var chat1 = Chat(11, false, false)
        val result = communication.createChat(chat1)
        assertEquals("$chat1", result.toString())
    }

    @Test
    fun createMessagesTest() {
        var chat1 = Chat(11, false, false)
        var message1 = DirectMessages(1, "AAA", false, false)
        val result = communication.createMessages(chat1, message1)
        assertEquals("$message1", result.toString())
    }

}