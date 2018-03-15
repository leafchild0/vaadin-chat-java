package com.leafchild0.chat

import com.leafchild0.chat.service.UserRepository
import com.leafchild0.chat.view.ChatView
import com.vaadin.annotations.*
import com.vaadin.navigator.Navigator
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.spring.navigator.SpringViewProvider
import com.vaadin.ui.UI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.annotation.EnableEventBus

/**
 * @author victor
 * @date 3/9/18
 */
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR)
@PreserveOnRefresh
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@Title("Vaadin Chat Events")
@Theme("chat")
@EnableEventBus
class VaadinChatUI : UI() {

	@Autowired private var viewProvider: SpringViewProvider? = null
	@Autowired private var eventBus: EventBus.ApplicationEventBus? = null
	@Autowired private var userRepository: UserRepository? = null

	override fun init(request: VaadinRequest) {

		setSizeFull()
		setCurrentUser()

		// Create a navigator to control the views
		val navigator = Navigator(this, this)
		navigator.addProvider(viewProvider!!)
		// KISS
		navigator.navigateTo(ChatView.VIEW_NAME)
	}

	private fun setCurrentUser() {

		val auth = SecurityContextHolder.getContext().authentication
		if (auth.principal != null && auth.principal is User) {
			val loggedUser = auth.principal as User
			val user = userRepository!!.findByUsername(loggedUser.username)
			Utils.currentUser = user
		}
	}

	companion object {

		private val current: VaadinChatUI
			get() = UI.getCurrent() as VaadinChatUI

		fun getEventBus(): EventBus.ApplicationEventBus
		{
			return current.eventBus!!
		}
	}
}
