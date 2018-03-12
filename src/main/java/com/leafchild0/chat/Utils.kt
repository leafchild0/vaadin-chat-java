package com.leafchild0.chat

import com.leafchild0.chat.data.MessageUser
import com.vaadin.server.VaadinSession
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

/**
 * @author victor
 * @date 3/9/18
 */
object Utils {

	private const val USER = "user"

	var currentUser: MessageUser
		get() = VaadinSession.getCurrent().session.getAttribute(USER) as MessageUser
	    set(user) = VaadinSession.getCurrent().session.setAttribute(USER, user)

}
