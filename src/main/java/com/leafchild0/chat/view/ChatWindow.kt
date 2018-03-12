package com.leafchild0.chat.view

import com.leafchild0.chat.Utils
import com.leafchild0.chat.VaadinChatUI
import com.leafchild0.chat.data.Message
import com.leafchild0.chat.data.MessageUser
import com.leafchild0.chat.events.MessageSentEvent
import com.leafchild0.chat.service.MessageRepository
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Sizeable
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*

/**
 * @author victor
 * @date 3/9/18
 */
internal class ChatWindow(private val repository: MessageRepository, private val users: List<MessageUser>) : Window() {

	private var recipient: MessageUser? = null

	private var mainLayout: HorizontalLayout? = null
	private var userList: VerticalLayout? = null
	private var conversation: VerticalLayout? = null

	init {

		buildLayout()
	}

	private fun buildLayout() {

		setPosition(800, 100)
		setWidth(600f, Sizeable.Unit.PIXELS)
		setHeight(600f, Sizeable.Unit.PIXELS)
		isResizable = true
		caption = " Chat Window 1.0"
		icon = VaadinIcons.USERS

		mainLayout = HorizontalLayout()
		mainLayout!!.setSizeFull()
		mainLayout!!.addStyleName("chat-main-layout")

		initUserList()
		val chatArea = initChatArea()

		mainLayout!!.addComponent(userList)
		mainLayout!!.addComponent(chatArea)
		mainLayout!!.setComponentAlignment(userList, Alignment.MIDDLE_LEFT)
		mainLayout!!.setComponentAlignment(chatArea, Alignment.MIDDLE_RIGHT)
		mainLayout!!.setExpandRatio(chatArea, .6f)

		content = mainLayout
	}

	private fun initChatArea(): VerticalLayout {

		val chatArea = VerticalLayout()
		chatArea.isSpacing = false
		chatArea.setMargin(false)
		chatArea.setSizeFull()
		chatArea.addStyleName("chat-area")

		val textArea = TextArea("Start chatting")
		textArea.rows = 4
		textArea.placeholder = "Type something here"

		val send = Button("Send")
		send.addStyleName("send-button")
		send.setWidth(100f, Sizeable.Unit.PIXELS)
		send.addClickListener { _ ->
			if (!textArea.value.isEmpty()) {
				sendMessage(textArea.value)
				textArea.clear()
			}
		}

		val layout = HorizontalLayout(textArea, send)
		layout.isSpacing = false
		layout.setMargin(false)
		layout.setHeight(150f, Sizeable.Unit.PIXELS)
		layout.setComponentAlignment(textArea, Alignment.MIDDLE_LEFT)
		layout.setComponentAlignment(send, Alignment.MIDDLE_RIGHT)
		layout.addStyleName("send-layout")

		conversation = VerticalLayout()
		conversation!!.setMargin(false)
		conversation!!.isSpacing = false
		conversation!!.setSizeFull()
		conversation!!.addStyleName("chat-conversation")

		chatArea.addComponent(conversation)
		chatArea.addComponent(layout)
		chatArea.setExpandRatio(conversation, 1f)

		return chatArea
	}

	private fun sendMessage(text: String) {

		val message = Message(Utils.currentUser, recipient!!, text)
		// Send event
		VaadinChatUI.current.eventBus!!.publish(this, MessageSentEvent(this, message))
		// Add message into conversation
		addMessageToConversation(message)

	}

	private fun addMessageToConversation(message: Message) {
		// Add message to conversation
		// On left if sender is user

	}

	private fun initUserList() {

		userList = VerticalLayout()
		userList!!.setMargin(false)
		userList!!.isSpacing = true
		userList!!.setHeight(100f, Sizeable.Unit.PERCENTAGE)
		userList!!.setWidth(150f, Sizeable.Unit.PIXELS)
		userList!!.addStyleName("user-list")
		userList!!.defaultComponentAlignment = Alignment.TOP_LEFT

		users.forEach { u ->

			if (u != Utils.currentUser) {
				val userTile = HorizontalLayout()
				userTile.setMargin(false)
				userTile.isSpacing = false
				userTile.addStyleName("user-tile")
				userTile.setHeight(40f, Sizeable.Unit.PIXELS)
				userTile.setWidth(100f, Sizeable.Unit.PERCENTAGE)

				// User icon with status

				// User name
				val label = Label()
				label.contentMode = ContentMode.HTML
				label.value = VaadinIcons.USER.html + " " + u.username
				label.addStyleName("user-tile-label")
				label.setSizeFull()
				userTile.addComponent(label)
				userTile.setComponentAlignment(label, Alignment.MIDDLE_CENTER)

				// Listener
				userTile.addLayoutClickListener { _ -> openChatWithUser(u, userTile) }

				userList!!.addComponent(userTile)
				userList!!.setExpandRatio(userTile, .1f)
			}
		}
	}

	private fun openChatWithUser(u: MessageUser, component: HorizontalLayout) {

		recipient = u
		component.addStyleName("user-selected")
		println(u.username)
		populateChatHistory()

	}

	private fun populateChatHistory() {
		// Get messages from repository
		// Add to conversation

	}
}
