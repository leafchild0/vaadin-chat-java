package com.leafchild0.chat.view

import com.leafchild0.chat.Utils
import com.leafchild0.chat.service.MessageRepository
import com.leafchild0.chat.service.UserRepository
import com.vaadin.navigator.View
import com.vaadin.shared.ui.ContentMode
import com.vaadin.spring.annotation.SpringView
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.events.EventBus

/**
 * @author victor
 * @date 3/9/18
 */
@SpringView(name = ChatView.VIEW_NAME)
@UIScope
class ChatView @Autowired
constructor(private val repository: MessageRepository,
	private val userRepository: UserRepository, private val applicationEventBus: EventBus.ApplicationEventBus) :
	VerticalLayout(), View {

	init {
		buildLayout()
	}

	private fun buildLayout() {

		setSizeFull()

		val label = Label("Hello <b>" + Utils.currentUser.username.toUpperCase() + "</b>")
		label.contentMode = ContentMode.HTML

		val button = Button("Click here to open the chat")
		button.addClickListener { _ -> UI.getCurrent().addWindow(ChatWindow(repository, applicationEventBus,
			userRepository.findAll())) }

		val logout = Button("Logout")
		logout.addClickListener { _ -> UI.getCurrent().page.setLocation("/logout") }

		val mainLayout = VerticalLayout(label, HorizontalLayout(button, logout))
		mainLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT)

		addComponent(mainLayout)
	}

	companion object {

		const val VIEW_NAME = "/chat"
	}
}
