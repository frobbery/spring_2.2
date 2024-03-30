package com.example.spring_22;

import com.example.spring_22.config.YamlPropertySourceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(value = "/application-test.yml", factory = YamlPropertySourceFactory.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
