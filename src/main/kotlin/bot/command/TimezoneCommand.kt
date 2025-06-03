package bot.command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import bot.callbackhandler.TimezoneCallbackHandler

object TimezoneCommand : Command {

    private const val COMMAND_NAME = "/timezone"

    private val timeZoneOffsets = listOf(
        "0",
        "+1",
        "+2",
        "+3",
        "+4",
        "+5",
        "+6",
        "+7",
        "+8",
        "+9",
        "+10",
        "+11",
        "+12",
        "+13",
        "-1",
        "-2",
        "-3",
        "-4",
        "-5",
        "-6",
        "-7",
        "-8",
        "-9",
        "-10",
        "-11",
        "-12"
    )

    override fun execute(message: Message, bot: Bot) {
        val chatId = message.chat.id

        val buttonsList = ArrayList<List<InlineKeyboardButton>>()
        for (timeZoneOffset in timeZoneOffsets) {
            buttonsList.add(
                listOf<InlineKeyboardButton>(
                    InlineKeyboardButton.CallbackData(
                        text = timeZoneOffset,
                        callbackData = TimezoneCallbackHandler.getQueryName() + " " + timeZoneOffset
                    )
                )
            )
        }

        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = "Выберите часовой пояс (UTC)",
            replyMarkup = InlineKeyboardMarkup.create(buttonsList)
        )

    }

    override fun getCommandName(): String {
        return COMMAND_NAME
    }

}