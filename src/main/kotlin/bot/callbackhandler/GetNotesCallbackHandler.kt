package bot.callbackhandler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import dao.NoteDao

object GetNotesCallbackHandler : CallbackHandler {
    private const val QUERY_NAME = "GET"

    override fun getQueryName(): String {
        return QUERY_NAME
    }

    override fun handle(callbackQuery: CallbackQuery, bot: Bot) {
        val id = callbackQuery.data.split(" ")[1]
        val chatId = callbackQuery.message?.chat?.id
        val note = NoteDao.findById(id.toLong())
        val buttonsList = ArrayList<InlineKeyboardButton>()
        buttonsList.add(
            InlineKeyboardButton.CallbackData(
                text = "Удалить",
                callbackData = DeleteCallbackHandler.getQueryName() + " " + id
            )
        )
        buttonsList.add(
            InlineKeyboardButton.CallbackData(
                text = "Установить время уведомления",
                callbackData = SetNotificationCallbackHandler.getQueryName() + " " + id
            )
        )
        if (chatId != null) {
            bot.sendMessage(
                ChatId.fromId(chatId),
                note?.text.orEmpty() + ' ' + note?.notificationTime.toString(),
                replyMarkup = InlineKeyboardMarkup.create(buttonsList)
            )
        }
    }
}