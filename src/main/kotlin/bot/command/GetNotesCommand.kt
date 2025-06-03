package bot.command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import dao.NoteDao
import bot.callbackhandler.GetNotesCallbackHandler

object GetNotesCommand : Command {

    private const val COMMAND_NAME = "/getnotes"

    override fun execute(message: Message, bot: Bot) {
        val notes = NoteDao.findAllByChatId(message.chat.id)
        val buttonsList = ArrayList<List<InlineKeyboardButton>>()
        for (note in notes) {
            buttonsList.add(
                listOf<InlineKeyboardButton>(
                    InlineKeyboardButton.CallbackData(
                        text = note.text,
                        callbackData = GetNotesCallbackHandler.getQueryName() + " " + note.id.toString()
                    )
                )
            )
        }
        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = "Сохранённые записи:",
            replyMarkup = InlineKeyboardMarkup.create(buttonsList)
        )
    }

    override fun getCommandName(): String {
        return COMMAND_NAME
    }

}