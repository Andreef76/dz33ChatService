import communication.chat1
import communication.chat2
import communication.chatKit
import communication.clear
import communication.getChats
import communication.getRecentChats
import communication.getUnreadChatsCount
import communication.message1
import communication.message2


object communication {
    var chatKit: MutableList<Chat> = arrayListOf()        // список чатов
    var message1 = DirectMessages(1, "AAA", false, false)
    var message2 = DirectMessages(2, "BBB", false, false)
    var message3 = DirectMessages(3, "FFF", false, false)
    var chat1 = Chat(11, false, false)
    var chat2 = Chat(12, false, false)


    fun clear() {
        chatKit.clear()
    }

    fun createChat(chat: Chat): Chat {     // создает чат
        chatKit.add(chat)
        return chatKit.last()
    }

    fun createMessages(chat: Chat, element: DirectMessages): DirectMessages { // создает сообщение чата, в случае если сообщение то сначала первое создает сам чат
        if (!chat.messageKit.contains(element)) {
            createChat(chat)
        }
        chat.messageKit.add(element)
        return chat.messageKit.last()
    }

    fun <T> edit(list: MutableList<T>, element: T): Boolean {     // редактирует сообщение в чате
        list.find { it == element } ?: throw NoteNotFoundException("Сообщение не найдено")//равны будут при равных id (изменённый метод equals), если не найдёт равных сразу вернёт false
        val index = list.indexOf(element) // вычисляем индекс подошедшего элемента
        list.removeAt(index) //удаляем элемент по индексу
        list.add(index, element) // добавляем элемент по индексу
        return true
    }

    fun <T> delete(list: MutableList<T>, element: T): Boolean {         // метод помечает как удаленные сообщение в чате или сам чат
        list.find { it == element }
            ?: return false //равны будут при равных id (изменённый метод equals), если не найдёт равных сразу вернёт false
        if (element is DirectMessages) {
            val forEditing =
                list.find { it == element } // здесь ищется потому что равны при равном id благодаря equals переназначенному
            forEditing as DirectMessages  // находит в списке элемент с таким же id и передаёт дальше уже его, а не входной ОК
            return edit(list as MutableList<DirectMessages>, forEditing.copy(isDelete = true))
        }
        if (element is Chat) {
            val forEditing =
                list.find { it == element } // Сначала найти такой объект в списке, а потом уже создавать копию
            forEditing as Chat
            return edit(list as MutableList<Chat>, forEditing.copy(isDelete = true))
        }
        return list.remove(element)
    }


    fun <T> reading(list: MutableList<T>, element: T): Boolean {         // метод проставляет отметку о прочтении сообщения в чате или само чата (помечает как прочтенный)
        list.find { it == element }
            ?: return false
        if (element is DirectMessages) {
            val forEditing = list.find { it == element }
            forEditing as DirectMessages
            return edit(list as MutableList<DirectMessages>, forEditing.copy(readMark = true))
        }
        if (element is Chat) {
            val forEditing = list.find { it == element }
            forEditing as Chat
            return edit(list as MutableList<Chat>, forEditing.copy(readMark = true))
        }
        return list.remove(element)
    }

    fun getChats() {                         //  возвращает список чатов если они есть
        if (chatKit.size > 0) chatKit.forEach { println(it) } else println("нет сообщений в чате")
    }

    fun getMessage(chat: Chat) {                         //  возвращает список сообщений в чате если они есть
        if (chat.messageKit.size > 0) chat.messageKit.forEach { println(it) } else println("нет сообщений в чате")
    }

    fun getChatsId(element: Int) {                         //  возвращает список сообщений из чата по Id
        if (chatKit.size > 0) {
            for (item in chatKit) {
                if (item.chatId == element)
                    println(item)
            }
        } else println("Чата с таким Id нет")
    }

    fun getRecentChats(chat: Chat, element: Int) {       //  возвращает список последних сообщений в количестве element (при указании ID последнего сообщения, возвращает сообщения начиная с которого нужно подгрузить более новые)
        for (item in chat.messageKit) {
            if (item.idMessage >= chat.messageKit.size - (element - 1))
                println(item)
        }
    }

    fun getMessageRead(chat: Chat, element: Int) {       //  После того как вызвана эта функция, все отданные сообщения автоматически считаются прочитанными
            println( chat.messageKit.takeLast(element).map { it.copy(readMark = true) })
    }

    fun getUnreadChatsCount(chat: Chat) {            // получение количества непрочитанных сообщений
        var quantity = 0
        for (item in chat.messageKit) {
            if (item.readMark == false)
                quantity++
        }
        println("количество непрочитанных сообщений чата составляет - $quantity")
    }
}
    fun main() {

//        communication.createChat(chat1)
//        communication.createChat(chat2)
        communication.createMessages(chat1, communication.message1)
        communication.createMessages(chat1, communication.message2)
        communication.createMessages(chat1, communication.message3)
        println(chat1.messageKit)
        println(chatKit)

        communication.edit(chat1.messageKit, message2.copy(text = "CCC"))
        communication.delete(chat1.messageKit, message2)
        communication.reading(chat1.messageKit, message1)
        communication.delete(chatKit, chat2)
           println(chatKit)

        getChats()
        getRecentChats(chat1, 1)
        getUnreadChatsCount(chat1)
        communication.getChatsId(12)
        communication.getMessageRead(chat1, 2)
    }





//Создать чат. Чат создаётся, когда пользователю отправляется первое сообщение.

