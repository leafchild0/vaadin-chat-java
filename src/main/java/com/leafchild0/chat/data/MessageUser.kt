package com.leafchild0.chat.data

import javax.persistence.*

/**
 * @author victor
 * @date 3/9/18
 */
@Entity
@Table(name = "users")
 data class MessageUser(
	@Column(name = "username") val username:String = "",
	@Column(name = "password") var password: String = "",
	@Id @GeneratedValue(strategy = GenerationType.AUTO) val id:Long = 0
)
