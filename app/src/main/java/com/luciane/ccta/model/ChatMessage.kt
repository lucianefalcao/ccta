package com.luciane.ccta.model

class ChatMessage(var id: String, var message: String, var memberId: String,
                  var timestamp: Long, var type: MessageType = MessageType.RIGHT) {

    enum class MessageType {
        LEFT, RIGHT
    }

    constructor() : this("", "", "", -1)
}