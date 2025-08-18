package kr.elroy.aigoya;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AigoyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AigoyaApplication.class, args);
	}

	@RestController
	class MyController {

		private final ChatClient chatClient;

		public MyController(ChatClient.Builder chatClientBuilder) {
			this.chatClient = chatClientBuilder.build();
		}

		@GetMapping("/ai")
		String generation(String userInput) {
			return this.chatClient.prompt()
					.user(userInput)
					.call()
					.content();
		}
	}

}
