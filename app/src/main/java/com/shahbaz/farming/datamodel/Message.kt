package com.shahbaz.farming.datamodel

data class Message(
    val text: String,
    val isBot: Boolean // true if the message is from the bot, false if it's from the user
)