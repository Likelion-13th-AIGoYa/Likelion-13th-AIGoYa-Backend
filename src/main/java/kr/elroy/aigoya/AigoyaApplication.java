package kr.elroy.aigoya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableCaching // ★★★ [추가] 캐싱 기능 활성화
@SpringBootApplication
public class AigoyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AigoyaApplication.class, args);
	}

}
