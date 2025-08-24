package kr.elroy.aigoya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AigoyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AigoyaApplication.class, args);
	}

}
