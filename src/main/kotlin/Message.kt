
data class Chat(
    val chatId: List<Int>,                  // id чата
    var messageKit: MutableList<DirectMessages> = mutableListOf()   // список сообщений чата
) {

    override fun equals(other: Any?): Boolean {
        val expected = if (other is Chat) other else return super.equals(other)
        return (this.chatId == expected.chatId)
    }
}
data class DirectMessages (
    val idMessage: Int,                  // id текущего сообщения
    val text: String,                   // текст сообщения
    val isDelete: Boolean,             // отметка об удалении сообщения. Tru - удалено
    val readMark: Boolean,             // отметка о прочтении сообщения. Tru - прочтено
    val authorId: Int = -1
) {
    override fun equals(other: Any?): Boolean {
        val expected = if (other is DirectMessages) other else return super.equals(other)
        return (this.idMessage == expected.idMessage)

    }
}