package ru.smak.chat

import java.net.Socket

class ConnectedClient(val socket: Socket) {

    private val communicator = Communicator(socket)
    private var userName: String? = null

    init {
        connectedClients.add(this)
        communicator.start { message -> parse(message) }
        communicator.sendMessage("Введите ник:")
    }

    private fun parse(message: String) {
        if (userName != null) {
            if(message == "/leave"){ //Полукостыль, а может быть и полный костыль, прокладывающий путь к командам в чате
                stop()
            } else {
                sendToAll("$userName: $message", echo = true)
            }

        } else {
            val usernameAttempt = message //читаемость
            var unavailable = false
            connectedClients.forEach {
                if(it !== this && it.userName?.equals(usernameAttempt) == true) unavailable = true
            }
            if (unavailable) {
                communicator.sendMessage("[!] Ник '$usernameAttempt' занят. Попробуйте снова:")
                return
            }

            userName = usernameAttempt
            communicator.sendMessage("[!] Установлен ник '$userName'.")
            sendToAll("$userName присоединился к чату.", echo = false)
        }
    }

    fun stop() {
        communicator.stop()
        connectedClients.remove(this)
        sendToAll("$userName покинул чат.", echo = false)
    }

    private fun sendToAll(message: String, echo: Boolean = true) {
        connectedClients.forEach {
            if (echo || it != this) it.communicator.sendMessage(message)
        }
    }

    companion object {
        private val connectedClients = mutableListOf<ConnectedClient>()
    }
}