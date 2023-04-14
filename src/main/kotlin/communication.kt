
import communication.chatKit
import communication.getChats
import communication.getLastMessages
//import communication.getRecentChats
import communication.getUnreadChatsCount


object communication {

    private var currentUserId: Int = 0
    var chatKit: MutableList<Chat> = mutableListOf()        // список чатов


    fun setCurrentUserId(id: Int) {
        currentUserId = id
    }
    fun clearChat() {
        chatKit.clear()
    }

    fun createChat(forUserId: Int): Chat {     // создает чат
        val newChat = Chat(sortedSetOf(forUserId, currentUserId).toList(), mutableListOf()) //currentUserId это мы, тот кто залогинился
        chatKit.add(newChat) //sorted упорядочит список из 2 чисел по возрастанию чтобы чаты не дублировались
        return chatKit.last()
    }

    fun createMessages(forUserId: Int, element: DirectMessages): DirectMessages { // создает сообщение чата, в случае если сообщение то сначала первое создает сам чат

        val chat = chatKit.find { it.chatId == sortedSetOf(forUserId, currentUserId).toList() }  // если такого нет вернёт null и сработает элвис
            ?: createChat(forUserId) // и создастся новый чат.
        chat.messageKit.add(element.copy(authorId = currentUserId)) // Проверили есть ли чат, если нет создали и в него уже записали сообщение //здесь лучше добавить поле автора сообщения
        return chat.messageKit.last() //так мы сможем разбивать потом сообщения на входящие и исходящие
        }

    fun <T> edit(list: MutableList<T>?, element: T): Boolean {     // редактирует сообщение в чате
        list?.find { it == element } ?: throw ChatNotFoundException("Сообщение не найдено")//равны будут при равных id (изменённый метод equals), если не найдёт равных сразу вернёт false
        val index = list.indexOf(element) // вычисляем индекс подошедшего элемента
        list.removeAt(index) //удаляем элемент по индексу
        list.add(index, element) // добавляем элемент по индексу
        return true
    }

    fun <T> delete(list: MutableList<T>?, element: T): Boolean {         // метод помечает как удаленные сообщение в чате или сам чат
        list?.find { it == element }
            ?: return false //равны будут при равных id (изменённый метод equals), если не найдёт равных сразу вернёт false
        if (element is DirectMessages) {
            val forEditing = list.find { it == element } // здесь ищется потому что равны при равном id благодаря equals переназначенному
            forEditing as DirectMessages  // находит в списке элемент с таким же id и передаёт дальше уже его, а не входной
            return edit(list as MutableList<DirectMessages>, forEditing.copy(isDelete = true))
        }
        if (element is Chat) {
            val forEditing =
                list.find { it == element } ?: return false // Сначала найти такой объект в списке, а потом уже создавать копию
            forEditing as Chat
            return list.remove(forEditing)
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
//        if (element is Chat) {
//            val forEditing = list.find { it == element }
//            forEditing as Chat
//            return edit(list as MutableList<Chat>, forEditing.copy(readMark = true))
//        }
        return false
    }

    fun getLastMessages() =
        getChats().map { chat: Chat ->
            if (chat.messageKit.size > 0) {
                if (chat.messageKit.last().authorId == currentUserId)
                "[ИСХ] ${chat.messageKit.last().text}"
                else "[ВХОД] ${chat.messageKit.last().text}"
            } else "нет сообщений"
        }


    fun getChats() : List<Chat> {                         //  возвращает список чатов если они есть
       // if (chatKit.size > 0) chatKit.forEach { println(it)
        return chatKit.map { chat: Chat ->  //Map возвращает список (другой), сам chatKit не изменится т.е мы возврощаем не наш список чатов
            chat.copy(messageKit =                           // мы тут перезаписываем наш, просто отфильрованный, его копию nf
            chat.messageKit.filter { message ->
                !message.isDelete
            }.toMutableList())
        }.filter { chat: Chat ->
            chat.chatId.contains(currentUserId)
        } // Чтобы залогиненый пользователь не видел чужие чаты что то я вообще плохо соображаю
//        if (chatKit.size > 0) {
//            for (item in chatKit) {
//                if (item.chatId.contains(currentUserId))
//                    var itemFilter = chatKit.filter { it.messageKit.filter { message -> !message.isDelete } }
////                    if (chatKit.filter { it.messageKit.filter { message -> !message.isDelete } })
//                    println(itemFilter)
//            }
//        } else println("нет сообщений в чате")
    }

    fun getMessage(chat: Chat) {                         //  возвращает список сообщений в чате если они есть
        if (chat.messageKit.size > 0) chat.messageKit.forEach { println(it) } else println("нет сообщений в чате")
    }

    fun getChatsId(element: Int) {                         //  возвращает список сообщений из чата по Id
        if (chatKit.size > 0) {
            for (item in chatKit) {
                if (item.chatId == sortedSetOf(currentUserId, element).toList())
                    println(item)
            }
        } else println("Чата с таким Id нет")
    }

//    fun getRecentChats(chat: Chat, element: Int) {       //  возвращает список последних сообщений в количестве element (при указании ID последнего сообщения, возвращает сообщения начиная с которого нужно подгрузить более новые)
//        for (item in chat.messageKit) {
//            if (item.idMessage >= chat.messageKit.size - (element - 1))
//                println(item)
//        }
//    }

//    fun getMessageRead(chat: Chat, element: Int) {       //  После того как вызвана эта функция, все отданные сообщения автоматически считаются прочитанными
////        var integer = 0
//
//        println( chat.messageKit.takeLast(element).map { it.copy(readMark = true) })
//
//    }

    fun getMessageRead(chat: Chat, element: Int, lastMessageId: Int = 0): List<DirectMessages> {
    val messages = if (lastMessageId > 0) {
        chat.messageKit.filter { it.idMessage > lastMessageId }.takeLast(element)
    } else {
        chat.messageKit.takeLast(element)
    }
    return messages.map { it.copy(readMark = true) }
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


var message1 = DirectMessages(1, "AAA", false, false)
var message2 = DirectMessages(2, "BBB", false, false)
var message3 = DirectMessages(3, "FFF", false, false)
var chat1 = Chat(listOf(0, 11))  // 2 айди, одного кто создаёт чат, другое кому?, они будут упорядочены по возрастанию при создании чата
var chat2 = Chat(listOf(0, 12))
    fun main() {
        communication.setCurrentUserId(15) // Сначала допустим логинимся под Id 15
        communication.createChat(11)
//        communication.createChat(2)
        communication.createMessages(55, message1)
        communication.edit(chatKit.find { it.chatId == sortedSetOf(15, 55).toList() }?.messageKit, message1.copy(text = "CCC"))
        communication.delete(chatKit.find { it.chatId == sortedSetOf(15, 55).toList() }?.messageKit, message1)
        communication.setCurrentUserId(32) // Потом под id 32, разные пользователи не могут смотреть сообщения к которым не причастны
        communication.createMessages(945, message2)
        communication.createMessages(123, message3)
//        println(chat1.messageKit)
//        println(chatKit)


        communication.reading(chat1.messageKit, message1)

        communication.getMessage(chat1)
        println()
        communication.setCurrentUserId(32) //Дальше просматриваем для id 15
        println(".........Test:")
        getChats().forEach { println(it) } // фильтр делает так, чтобы можно было просматривать только те чаты, в которых участвовал
//        println("..........End Test............")
//        println()
//        chatKit.forEach{ println(it) }
        println("..........End Test............")
        println()
        getLastMessages().forEach{ println(it) }
        println("..........End Test............")
        println()
//        getRecentChats(chat1, 3)
        getUnreadChatsCount(chat1)
        communication.getChatsId(12)
        println( communication.getMessageRead(chatKit[1], 2))
        getChats()
    }



