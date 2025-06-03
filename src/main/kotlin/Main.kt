import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.extensions.filters.Filter
import entity.NoteTable
import entity.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import bot.MessageHandler
import bot.NotificationScheduler.checkNotificationTime
import bot.callbackhandler.DeleteCallbackHandler
import bot.callbackhandler.GetNotesCallbackHandler
import bot.callbackhandler.SetNotificationCallbackHandler
import bot.callbackhandler.TimezoneCallbackHandler
import bot.command.DisableCommand
import bot.command.GetNotesCommand
import bot.command.TimezoneCommand
import kotlin.concurrent.thread

enum class ConversationState { WAITING_NOTIFICATION_TIME }

val userStates = mutableMapOf<Long, ConversationState>()

val callbackMap = mapOf(
    Pair(GetNotesCallbackHandler.getQueryName(), GetNotesCallbackHandler),
    Pair(DeleteCallbackHandler.getQueryName(), DeleteCallbackHandler),
    Pair(SetNotificationCallbackHandler.getQueryName(), SetNotificationCallbackHandler),
    Pair(TimezoneCallbackHandler.getQueryName(), TimezoneCallbackHandler)
)

val commandMap = mapOf(
    Pair(DisableCommand.getCommandName(), DisableCommand),
    Pair(GetNotesCommand.getCommandName(), GetNotesCommand),
    Pair(TimezoneCommand.getCommandName(), TimezoneCommand)
)

val BOT_TOKEN = ""

private fun initDatabase() {
    Database.connect(url = "jdbc:sqlite:note.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(NoteTable, UserTable)
        commit()
    }
}

private fun initBot(): Bot {
    return bot {
        this.token = BOT_TOKEN
        dispatch {
            callbackQuery {
                val query = callbackQuery.data.split(" ")[0]
                callbackMap[query]?.handle(callbackQuery, bot)
            }
            message(Filter.Command) {
                val command = message.text
                commandMap[command]?.execute(message, bot)
            }
            message(!Filter.Command) {
                MessageHandler.handleMessage(message, bot)
            }
        }
    }
}



fun main() {
    initDatabase()
    val bot = initBot()
    thread {
        checkNotificationTime(bot)
    }
    bot.startPolling()
}

