package com.leafchild0.chat

import com.leafchild0.chat.data.MessageUser
import com.leafchild0.chat.service.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = ["com.leafchild0.chat"])
open class VaadinChatApplication {

	@Bean
	open fun loadData(repository: UserRepository) = CommandLineRunner {

		// save a couple of users for testing
		repository.save(MessageUser("john", "john666"))
		repository.save(MessageUser("lucy", "test123"))
		repository.save(MessageUser("admin", "admin"))
		repository.save(MessageUser("user", "user"))

		log.info("Few dummy users were added")
	}

	companion object {

		private val log = LoggerFactory.getLogger(VaadinChatApplication::class.java)

		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(VaadinChatApplication::class.java, *args)
		}
	}
}
