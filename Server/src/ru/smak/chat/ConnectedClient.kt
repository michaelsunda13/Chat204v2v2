package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class ConnectedClient(val socket: Socket) {

    private val communicator = Communicator(socket)

    init{
        communicator.start { message -> parse(message) }
    }

    private fun parse(message: String){
        communicator.sendMessage(message)
    }

    fun stop(){
        communicator.stop()
    }
}