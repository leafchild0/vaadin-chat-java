package com.leafchild0.chat.view

import com.leafchild0.chat.Utils
import com.leafchild0.chat.VaadinChatUI
import com.leafchild0.chat.events.MessageSentEvent
import com.leafchild0.chat.events.UserLoggedIn
import com.leafchild0.chat.service.MessageRepository
import com.leafchild0.chat.service.UserRepository
import com.vaadin.navigator.View
import com.vaadin.shared.ui.ContentMode
import com.vaadin.spring.annotation.SpringView
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.annotation.EventBusListenerMethod

/**
 * @author victor
 * @date 3/9/18
 */
@SpringView(name = ChatView.VIEW_NAME)
@UIScope
class ChatView @Autowired
constructor(private val repository: MessageRepository,
	private val userRepository: UserRepository) :
	VerticalLayout(), View {

	private var chatWindow: ChatWindow? = null

	init {
		buildLayout()
	}

	private fun buildLayout() {

		setSizeFull()

		val label = Label("Hello <b>" + Utils.currentUser.username.toUpperCase() + "</b>")
		label.contentMode = ContentMode.HTML

		val button = Button("Click here to open the chat")
		button.addClickListener { _ ->
			chatWindow = ChatWindow(repository, VaadinChatUI.getEventBus(),
				userRepository.findAll())
			UI.getCurrent().addWindow(chatWindow)
		}

		val logout = Button("Logout")
		logout.addClickListener { _ ->
			VaadinChatUI.getEventBus().publish(this, UserLoggedIn(this, Utils.currentUser, false))
			UI.getCurrent().page.setLocation("/logout")
		}

		val mainLayout = VerticalLayout(label, HorizontalLayout(button, logout))
		mainLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT)

		addComponent(mainLayout)
	}

	override fun attach() {

		super.attach()
		VaadinChatUI.getEventBus().subscribe(this)
	}

	override fun detach() {

		super.detach()
		VaadinChatUI.getEventBus().unsubscribe(this)
	}

	@EventBusListenerMethod
	fun onNewMessage(event: MessageSentEvent) {
		// Add message only if it's for current user
		if (chatWindow != event.source) {
			chatWindow?.addMessageToConversation(event.message, false)
		}
	}

	@EventBusListenerMethod
	fun onNewUser(event: UserLoggedIn) {
		// Add message only if it's for current user
		if (chatWindow != event.source) {
			chatWindow?.setUserActive(event.user, event.loggedIn)
		}
	}

	companion object {

		const val VIEW_NAME = "/chat"
	}
}
