package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class ConnectedClient(val socket: Socket) {

    private var isRunning = true
    private val reader = Scanner(socket.getInputStream())
    private val writer = PrintWriter(socket.getOutputStream())

    init{
        startMessageAccepting()
    }

    private fun startMessageAccepting(){
        thread {
            while (isRunning){
                try {
                    val message = reader.nextLine()
                    parse(message)
                } catch (_: Throwable){}
            }
            socket.close()
        }
    }

    fun sendMessage(message: String){
        writer.println(message)
        writer.flush()
    }

    private fun parse(message: String){
        println("Клиент хотел сказать: $message")
        sendMessage(message)
    }

    fun stop(){
        isRunning = false
    }
}