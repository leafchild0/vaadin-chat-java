package com.leafchild0.chat.service

import com.leafchild0.chat.data.Message
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author victor
 * @date 3/9/18
 */
@SpringComponent
@UIScope
interface MessageRepository : JpaRepository<Message, Long>
