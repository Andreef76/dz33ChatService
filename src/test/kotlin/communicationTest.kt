import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class communicationTest {
    @Before
    fun fill() {
        communication.clearChat()
    }

    @Test
    fun createChatTest() {
        var chat1 = Chat(listOf(0, 11))
        var chat2 = Chat(listOf(0, 12))
        val result = communication.createChat(11)
        assertEquals("$chat1", result.toString())
    }

    @Test
    fun createMessagesTest() {
        var message1 = DirectMessages(1, "AAA", false, false, authorId = 0)
        var message2 = DirectMessages(2, "BBB", false, false)
        val result = communication.createMessages(15, message1)
        assertEquals("$message1", result.toString())
    }

}