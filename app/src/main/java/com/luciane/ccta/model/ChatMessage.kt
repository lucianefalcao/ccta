package com.luciane.ccta.model

class ChatMessage(var id: String, var text: String, var fromId: String,
                  var toId: String, var sendAtDateTime: String,
                  var type: MessageType = MessageType.RIGHT) {

    enum class MessageType {
        LEFT, RIGHT
    }

    constructor() : this("", "", "", "", "")
}