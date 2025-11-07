package com.example.petcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        holder.messageText.text = message.text

        if (message.isUser) {
            // This line is used for User message - align to right with blue background
            holder.messageContainer.setBackgroundResource(R.drawable.chat_bubble_user)
            (holder.messageContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginStart = dipToPx(holder.itemView.context, 50)
                marginEnd = dipToPx(holder.itemView.context, 8)
            }
        } else {
            // This line i have used for Bot message - align to left with gray background
            holder.messageContainer.setBackgroundResource(R.drawable.chat_bubble_bot)
            (holder.messageContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginStart = dipToPx(holder.itemView.context, 8)
                marginEnd = dipToPx(holder.itemView.context, 50)
            }
        }
    }

    override fun getItemCount() = messages.size

    private fun dipToPx(context: android.content.Context, dip: Int): Int {
        return (dip * context.resources.displayMetrics.density).toInt()
    }
}