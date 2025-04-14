package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import javax.annotation.processing.Messager
import kotlin.concurrent.thread

class Client(
    val host: String,
    val port: Int,
) {

    private val communicator = Communicator(Socket(host, port))

    init {

        communicator.start(::parse)

        while(communicator.isRunning) {
            val userScanner = Scanner(System.`in`)
            val userInput = userScanner.nextLine()
            if (userInput.isNotBlank())
                communicator.sendMessage(userInput)
            else
                stop()
        }
    }

    private fun parse(message: String){
        println(message)
    }

    fun stop(){
        communicator.stop()
    }
}