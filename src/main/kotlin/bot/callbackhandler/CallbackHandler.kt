package bot.callbackhandler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery

interface CallbackHandler {
    fun getQueryName(): String
    fun handle(callbackQuery: CallbackQuery, bot: Bot)
}