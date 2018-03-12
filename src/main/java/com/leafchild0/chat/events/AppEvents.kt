package com.leafchild0.chat.events

import com.leafchild0.chat.data.Message
import com.leafchild0.chat.data.MessageUser
import org.springframework.context.ApplicationEvent

/**
 * @author victor
 * @date 3/11/18
 */
/**
 * Event to notify new message was sent
 */
class MessageSentEvent(source: Any, val message: Message) : ApplicationEvent(source)

/**
 * Event to notify others that user has been logged in/out
 */
class UserLoggedIn(source: Any, val user: MessageUser, val loggedIn: Boolean) : ApplicationEvent(source)
