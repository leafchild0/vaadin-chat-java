package com.leafchild0.chat.data

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.persistence.*

/**
 * @author victor
 * @date 3/9/18
 */
@Entity
@Table(name = "messages")
data class Message(
	@JoinColumn(name = "author") @ManyToOne val author: MessageUser,
	@JoinColumn(name = "recipient") @ManyToOne val recipient: MessageUser,
	@Column(name = "body", length = 512) val body: String,
	@Column(name = "created_date") @DateTimeFormat var createdDate: LocalDate = LocalDate.now(),
	@Id @GeneratedValue val id: Long = 0


)
