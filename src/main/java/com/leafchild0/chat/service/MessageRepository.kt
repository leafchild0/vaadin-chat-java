package com.leafchild0.chat.service

import com.leafchild0.chat.data.Message
import com.leafchild0.chat.data.MessageUser
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * @author victor
 * @date 3/9/18
 */
@SpringComponent
@UIScope
interface MessageRepository : JpaRepository<Message, Long> {

	@Query("select m from Message m where m.author=:author and m.recipient=:recipient or m.author=:recipient and " +
		"m.recipient=:author")
	fun findByAuthorAndRecipient(author: MessageUser?, recipient: MessageUser?): List<Message>
}
