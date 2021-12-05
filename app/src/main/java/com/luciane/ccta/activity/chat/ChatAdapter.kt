package com.luciane.ccta.activity.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.R
import com.luciane.ccta.model.ChatMessage
import com.luciane.ccta.utils.DateTimeFormatter

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CHATTO = 0
    private val CHATFROM = 1

    private val messageList: MutableList<ChatMessage> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == CHATTO){
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_to_row, parent, false)

            return ChatItem(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_from_row, parent, false)

            return ChatItem(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = this.messageList[position]
        val text = message.message
        val sendAtDateTime = DateTimeFormatter.getFormatedDateTimeFromMilliseconds(message.timestamp)

        if(holder.itemViewType == CHATTO){
            val viewItem = holder as ChatItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatTo).text = text
            viewItem.itemView
                .findViewById<TextView>(R.id.textViewSendAtDateTimeChatTo).text = sendAtDateTime

        } else{
            val viewItem = holder as ChatItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatFrom).text = text
            viewItem.itemView
                .findViewById<TextView>(R.id.textViewSendAtDateTimeChatFrom).text = sendAtDateTime
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = this.messageList[position]
        if(message.type.equals(ChatMessage.MessageType.LEFT)){
            return CHATFROM
        }else {
            return CHATTO
        }
    }

    fun addMessage(chatMessage: ChatMessage) {
        this.messageList.add(chatMessage)
        notifyDataSetChanged()
    }

    class ChatItem internal constructor(view: View) : RecyclerView.ViewHolder(view)
}