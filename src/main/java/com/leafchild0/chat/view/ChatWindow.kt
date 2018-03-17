package com.leafchild0.chat.view

import com.leafchild0.chat.Utils
import com.leafchild0.chat.data.Message
import com.leafchild0.chat.data.MessageUser
import com.leafchild0.chat.events.MessageSentEvent
import com.leafchild0.chat.service.MessageRepository
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Sizeable
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*
import org.vaadin.spring.events.EventBus
import java.time.format.DateTimeFormatter

/**
 * @author victor
 * @date 3/9/18
 */
internal class ChatWindow(private val repository: MessageRepository, private val applicationEventBus: EventBus
.ApplicationEventBus, private val users: List<MessageUser>) : Window() {

	private var recipient: MessageUser? = null

	private var mainLayout: HorizontalLayout? = null
	private var userLayout: VerticalLayout? = null
	private var userList: MutableMap<MessageUser, Label> = HashMap()
	private var conversation: VerticalLayout? = null
	private val send: Button = Button("Send")
	private val panel = Panel()

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

		mainLayout!!.addComponent(userLayout)
		mainLayout!!.addComponent(chatArea)
		mainLayout!!.setComponentAlignment(userLayout, Alignment.MIDDLE_LEFT)
		mainLayout!!.setComponentAlignment(chatArea, Alignment.MIDDLE_RIGHT)
		mainLayout!!.setExpandRatio(chatArea, .6f)

		content = mainLayout
	}

	private fun initChatArea(): VerticalLayout {

		val chatArea = VerticalLayout()
		chatArea.isSpacing = false
		chatArea.setMargin(true)
		chatArea.setSizeFull()
		chatArea.addStyleName("chat-area")

		val textArea = TextField()
		textArea.setWidth(100f, Sizeable.Unit.PERCENTAGE)
		textArea.placeholder = "Type something here"

		send.addStyleName("send-button")
		send.setWidth(100f, Sizeable.Unit.PIXELS)
		send.isEnabled = recipient != null
		send.setClickShortcut(ShortcutAction.KeyCode.ENTER)
		send.addClickListener { _ ->
			if (!textArea.value.isEmpty() && recipient != null) {
				sendMessage(textArea.value)
				textArea.clear()
			}
		}

		val layout = HorizontalLayout(textArea, send)
		layout.isSpacing = false
		layout.setMargin(false)
		layout.setComponentAlignment(textArea, Alignment.MIDDLE_LEFT)
		layout.setExpandRatio(textArea, 1f)
		layout.setComponentAlignment(send, Alignment.MIDDLE_RIGHT)
		layout.addStyleName("send-layout")

		conversation = VerticalLayout()
		conversation?.setMargin(true)
		conversation?.isSpacing = false

		panel.setSizeFull()
		panel.content = conversation

		val panelBody = VerticalLayout()
		panelBody.setMargin(false)
		panelBody.isSpacing = false
		panelBody.setSizeFull()
		panelBody.addComponent(panel)
		panelBody.addStyleName("chat-conversation")

		chatArea.addComponent(panelBody)
		chatArea.addComponent(layout)
		chatArea.setExpandRatio(panelBody, 1f)

		return chatArea
	}

	private fun sendMessage(text: String) {

		val message = Message(Utils.currentUser, recipient!!, text)

		UI.getCurrent().access({
			// Save new message directly
			repository.save(message)
		})

		// Send event
		applicationEventBus.publish(this, MessageSentEvent(this, message))
		// Add message into conversation
		addMessageToConversation(message)

	}

	fun addMessageToConversation(message: Message, isAuthorMessage: Boolean = true) {
		// Add message to conversation
		// On left if sender is user

		val messageLayout = VerticalLayout()
		messageLayout.setWidthUndefined()
		messageLayout.setHeight(80f, Sizeable.Unit.PIXELS)
		messageLayout.isSpacing = false
		messageLayout.setMargin(false)
		if(isAuthorMessage) messageLayout.addStyleName("conversation-message-author")
		else messageLayout.addStyleName("conversation-message-recipient")

		val text = Label(message.body)
		val author = Label(message.author.username)
		author.addStyleName("conversation-message-author-name")
		val sendDate = Label(message.createdDate.format(DateTimeFormatter.ofPattern("dd-MM-yy")))
		sendDate.addStyleName("conversation-message-date")

		messageLayout.addComponents(author, text, sendDate)
		messageLayout.setExpandRatio(text, .5f)
		messageLayout.setComponentAlignment(text, Alignment.MIDDLE_CENTER)
		conversation?.addComponent(messageLayout)
		conversation?.setExpandRatio(messageLayout, 0f)

		if(isAuthorMessage) conversation?.setComponentAlignment(messageLayout, Alignment.TOP_LEFT)
		else conversation?.setComponentAlignment(messageLayout, Alignment.TOP_RIGHT)

		conversation?.markAsDirtyRecursive()

		scrollIntoView()
	}

	private fun initUserList() {

		userLayout = VerticalLayout()
		userLayout!!.setMargin(false)
		userLayout!!.isSpacing = true
		userLayout!!.setHeight(100f, Sizeable.Unit.PERCENTAGE)
		userLayout!!.setWidth(150f, Sizeable.Unit.PIXELS)
		userLayout!!.addStyleName("user-list")
		userLayout!!.defaultComponentAlignment = Alignment.TOP_LEFT

		users.forEach { u ->

			if (u != Utils.currentUser) {
				val userTile = HorizontalLayout()
				userTile.setMargin(false)
				userTile.isSpacing = false
				userTile.addStyleName("user-tile")
				userTile.setHeight(40f, Sizeable.Unit.PIXELS)
				userTile.setWidth(100f, Sizeable.Unit.PERCENTAGE)

				// User name
				val label = Label()
				label.contentMode = ContentMode.HTML
				label.value = VaadinIcons.USER.html + " " + u.username
				label.addStyleName("user-tile-label")
				label.setSizeFull()
				userTile.addComponent(label)
				userTile.setComponentAlignment(label, Alignment.MIDDLE_CENTER)

				// Listener
				userTile.addLayoutClickListener { _ ->
					recipient = u
					openChatWithUser(userTile)
					send.isEnabled = true
				}

				userLayout?.addComponent(userTile)
				userLayout?.setExpandRatio(userTile, .1f)

				userList[u] = label
			}
		}
	}

	fun setUserActive(user:MessageUser, active:Boolean) {

		if (active) userList[user]?.addStyleName("user-online")
		else userList[user]?.removeStyleName("user-online")
		userLayout?.markAsDirtyRecursive()
	}

	private fun openChatWithUser(component: HorizontalLayout) {

		component.addStyleName("user-selected")
		userLayout?.iterator()?.forEachRemaining { c ->
			if (c != component) c.removeStyleName("user-selected")
		}
		populateChatHistory()

	}

	private fun populateChatHistory() {

		conversation?.removeAllComponents()
		// Get messages from repository
		// Find all by recipient and author
		val threadMessages = repository.findByAuthorAndRecipient(recipient, Utils.currentUser)

		threadMessages.forEach({ m -> addMessageToConversation(m, m.author == Utils.currentUser) })

		scrollIntoView()
	}

	private fun scrollIntoView() {
		if (conversation!!.ui != null && conversation!!.componentCount > 0)
			conversation!!.ui.scrollIntoView(conversation?.getComponent(conversation!!.componentCount - 1))
	}
}
